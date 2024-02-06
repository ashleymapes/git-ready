
// change example here
import javax.swing.*;
import java.awt.Dimension;

class Simulation {

    public static boolean checkPerfectSquare( int num ) {
        int sqrt = (int)Math.floor( Math.sqrt(num) );
        if( (sqrt * sqrt) == num ) {
            return true;
        }
        return false;

    }
    public static String printArr( char[][] arr )
    {
        String output = "";
        for( int i = 0; i < arr.length; i++ )
        {
            for( int j = 0; j < arr[0].length ; j++ )
            {
                output += arr[i][j] + "    ";
            }
            output += "\n";
        }
        return output;
    }

    public static double[] getCountUp ( char[][] arr)
    {
        double[] countUp = new double[4];
        int numI = 0;
        int numR = 0;
        int numS = 0;
        double ratio;
        for( int i = 0; i < arr.length; i++ )
        {
            for( int j = 0; j < arr[0].length ; j++ )
            {
                if( arr[i][j] == 'I')
                    numI++;
                else if( arr[i][j] == 'R')
                    numR++;
                else
                    numS++;
            }
        }
        ratio = ((double)numI / (double)( arr.length * arr.length));
        countUp[0] = numI;
        countUp[1] = numR;
        countUp[2] = numS;
        countUp[3] = ratio;
        return countUp;
        
    }

    public static void checkIndividual( char[][] r, int row, int col, double infection, double recovery){
        int numInfectedAround = 0;
        double probability;
        double randomProbability;
        if( r[row][col] == 'S'){
            if( row-1 > 0 && r[row-1][col] == 'I') //checking above
                numInfectedAround++;
            if( row+1 < r.length && r[row+1][col] == 'I') //checking below
                numInfectedAround++;
            if( col-1 > 0 && r[row][col-1] == 'I') //checking left
                numInfectedAround++;
            if( col + 1 < r.length && r[row][col+1] == 'I') //checking right
                numInfectedAround++;

            probability = numInfectedAround * infection;
            if( probability > 1 )
                probability = 1;
            randomProbability = Math.random();
            if( randomProbability <= probability )
                r[row][col] = 'I';

        }
        else if( r[row][col] == 'I')
        {
            randomProbability = Math.random();
            if( randomProbability <= recovery )
                r[row][col] = 'R';
        }
    }
    
    public static void main( String[] args ){
        char[][] room;
        int numPeople;
        double infectionRate;
        double recoveryRate;
        int timeStep;
        int i;
        int j;
        int t;

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(500,500));
        
        frame.setLocationRelativeTo(null);

        while(true) {
            try{
                numPeople = Integer.parseInt( javax.swing.JOptionPane.showInputDialog(frame,"Please enter the number of people (must be a perfect square)") );
                if( !checkPerfectSquare(numPeople) )
                    throw new Exception();
                else 
                    break;
            }
            catch( NumberFormatException exception ){
                JOptionPane.showMessageDialog(frame, "ERROR: input must be an integer", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            catch( Exception e ){
                JOptionPane.showMessageDialog(frame, "ERROR: input must be a perfect square", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }

        while(true) {
            try{
                infectionRate = Double.parseDouble(javax.swing.JOptionPane.showInputDialog(frame,"Please enter an infection rate (must be between 0 and 1)"));
                if( infectionRate > 1 || infectionRate < 0 )
                    throw new Exception();
                else 
                    break;
            }
            catch( NumberFormatException exception ){
               JOptionPane.showMessageDialog(frame, "ERROR: input must enter a decimal", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            catch( Exception e ){
                JOptionPane.showMessageDialog( frame, "ERROR: input must be between 0 and 1 inclusive", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }

        while(true) {
            try{
                recoveryRate = Double.parseDouble(javax.swing.JOptionPane.showInputDialog(frame, "Please enter an recovery rate (must be between 0 and 1)"));
                if( recoveryRate > 1 || recoveryRate < 0 )
                    throw new Exception();
                else 
                    break;
            }
            catch( NumberFormatException exception ){
                JOptionPane.showMessageDialog( frame, "ERROR: input must be decimal", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            catch( Exception e ){
                JOptionPane.showMessageDialog( frame, "ERROR: input must be between 0 and 1 inclusive", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }

        while(true) {
            try{
                timeStep = Integer.parseInt(javax.swing.JOptionPane.showInputDialog(frame, "Please enter a time step (input must be at least 1) "));
                if( timeStep <= 0 )
                    throw new Exception();
                else 
                    break;
            }
            catch( NumberFormatException exception ){
                JOptionPane.showMessageDialog( frame, "ERROR: input must an integer", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            catch( Exception e ){
                JOptionPane.showMessageDialog( frame,"ERROR: input must be greater than 0", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }

        room = new char[(int)Math.sqrt(numPeople)][(int)Math.sqrt(numPeople)];

        // set random position for patient zero
        int x = (int)(Math.random() * room.length);
        int y = (int)(Math.random() * room.length);
        room[x][y] = 'I';

        // populate original room with all susceptible individuals (besides patient zero)
        for( i = 0; i < room.length; i++ )
        {
            for( j = 0; j < room[0].length ; j++ )
            {
                if( i != x || j != y )
                {
                    room[i][j] = 'S';
                }
            }
        }
        String message = printArr(room);

        JOptionPane.showMessageDialog(frame, message, "Original room", JOptionPane.PLAIN_MESSAGE);
        // print original room with the random infected individual

        for( t = 0; t < timeStep; t++ )
        {
            for( i = 0; i < room.length; i++ )
            {
                for( j = 0; j < room[0].length ; j++ )
                {
                    checkIndividual(room, i, j, infectionRate, recoveryRate);
                
                }
            }

            double[] totals = getCountUp(room);

            message = printArr(room);
            
            String otherMessage = "\n\nTotals for time step " + (t+1) + ":" +
                                "\n    Number of infected individuals: " + (int)totals[0] +
                                "\n    Number of recovered individuals: " + (int)totals[1] +
                                "\n    Number of susceptible individuals: " + (int)totals[2] +
                                "\n    Ratio of infected to total: " + totals[3];
            JOptionPane.showMessageDialog(frame, message+ otherMessage, "Time step: " + (t+1), JOptionPane.PLAIN_MESSAGE);
            
            // try{
            //     String fileName = "timeStep" + (t+1) + ".txt";


            //     // print on general file
            //     FileWriter data = new FileWriter( "dataFile.txt", true );
            //     PrintWriter dataWriter = new PrintWriter( data );

            //     dataWriter.println("Totals for time step " + (t+1) + ":");
            //     dataWriter.println("\tNumber of infected individuals: " + (int)totals[0]);
            //     dataWriter.println( "\tNumber of recovered individuals: " + (int)totals[1]);
            //     dataWriter.println( "\tNumber of susceptible individuals: " + (int)totals[2]);
            //     dataWriter.printf( "\tRatio of infected to total: %.4f\n\n", totals[3]);

            //     // print in separate files
            //     FileWriter separateData = new FileWriter(fileName, true);
            //     PrintWriter separateWriter = new PrintWriter( separateData);
            //     separateWriter.println("Totals for time step " + (t+1) + ":");
            //     separateWriter.println("\tNumber of infected individuals: " + (int)totals[0]);
            //     separateWriter.println( "\tNumber of recovered individuals: " + (int)totals[1]);
            //     separateWriter.println( "\tNumber of susceptible individuals: " + (int)totals[2]);
            //     separateWriter.printf( "\tRatio of infected to total: %.4f\n\n", totals[3]);
                
            //     dataWriter.close();
            //     separateWriter.close();
            // }
            // catch( IOException e){
            //     System.out.println( "An error has occured");
            // }
        
        }


        JOptionPane.showMessageDialog(frame, "The pandemic has ended","THE END", JOptionPane.PLAIN_MESSAGE);
    
    }

}