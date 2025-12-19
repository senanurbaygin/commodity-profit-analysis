
// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int [][][] profit =new int [MONTHS][DAYS][COMMS];
    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        for (int m = 0 ; m < MONTHS; m++){
            Scanner reader = null;
            try {
                //open files
                reader = new Scanner( new java.io.File("Data_Files/" + months[m] + ".txt"));
                while (reader.hasNextLine()) {
                    // there should be 3 parts: day, commodity, profit
                    String[] p = reader.nextLine().split(",");
                    if (p.length !=3) continue;

                    int day;
                    int profitVal; // profit value on that day
                    try {
                        day=Integer.parseInt(p[0].trim());  // delete spaces and change values from string to int
                        profitVal =Integer.parseInt(p[2].trim());
                    }catch (Exception e) {
                        continue;// skip invalid lines
                    }
                    if (day<1 || day>DAYS) continue;
                    for (int c = 0; c < COMMS; c++){    //case-sensitive exact match
                        if (commodities[c].equals(p[1].trim())){
                            profit[m][day-1][c]=profitVal;    // [day-1]: 1-28 > 0-27
                            break;
                        }
                    }
                }
            }catch (Exception e) {
                //skip invalid files
            }finally{
                if (reader != null) {reader.close();
                }
            }
        }
    }



    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }
        int mostProfitVal = 0;
        for (int d =0;d<DAYS;d++){
            mostProfitVal += profit[month][d][0]; //index 0 is the initial reference
        }
        int bestCommodity = 0;
        for (int c=0;c<COMMS;c++) {  // calculate total profit for each commodity
            int sum = 0;
            for (int d=0;d<DAYS;d++){   //calculate total profit for the commodity
                sum+=profit[month][d][c]; }
            if (sum>mostProfitVal){     //update best commodity if sum is larger
                mostProfitVal=sum;
                bestCommodity = c; }
        }
        return commodities[bestCommodity] + " " + mostProfitVal ;
    }


    public static int totalProfitOnDay(int month, int day) {
        if (month<0 || month >=  MONTHS){
            return -99999;}
        if (day<1 || day > DAYS){
            return -99999;}
        int d= day - 1; //convert day to array index
        int totProfitsAllComms = 0;
        for (int c=0; c<COMMS;c++){
            totProfitsAllComms+= profit[month][d][c];
        }
        return totProfitsAllComms;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        //invalid checks
        if (commodity == null)
            return -99999;
        if (from < 1 || from > DAYS || to < 1 || to > DAYS)
            return -99999;
        if (from > to)
            return -99999;
        //case-sensitive match
        int commIndex = -1;   //initial value=-1 commIndex will be updated
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) {
                commIndex = i;
                break;
            }
        }
        if (commIndex == -1) return -99999;
        int total = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = from; d <= to; d++) {
                // days start from 1, but index should start from zero
                total += profit[m][d - 1][commIndex];   // day-1 = 1-DAYS > 0-DAYS-1
            }
        }

        return total;
    }
    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return -1;
        }
        int bestDay = 1;
        int maxProfit =0;
        for (int c = 0; c < COMMS ; c++){
            maxProfit+=profit[month][0][c]; //day1 is initial reference
        }
        for (int day = 2 ; day <= DAYS; day++) {
            int d = day - 1;
            int sum = 0;
            for (int c = 0; c < COMMS; c++) {
                sum += profit[month][d][c];
            }
            if (sum > maxProfit) {
                maxProfit = sum;
                bestDay = day;
            }
        }
        return bestDay;
    }
    public static String bestMonthForCommodity(String comm) {
        if(comm == null) { return  "INVALID_COMMODITY";}   //invalid commodity

        int commIndex = -1; //exact match
        for (int c=0 ; c<COMMS; c++){
            if (commodities[c].equals(comm)){
                commIndex = c ;
                break;
            }
        }
        if (commIndex ==-1 )  {return "INVALID_COMMODITY";}

        int bestMonthIndex=0; //reference is the first month's index
        int bestTotal = 0;
        for (int d=0; d< DAYS;d++){
            bestTotal+=profit[0][d][commIndex];
        }
        for (int m=1; m < MONTHS;m++){
            int total = 0;
            for (int d = 0; d<DAYS; d++){
                total+= profit[m][d][commIndex];
            }
            if (total>bestTotal){
                bestTotal=total;
                bestMonthIndex=m;
            }
        }
        return months[bestMonthIndex];  // method returns String
    }

    public static int consecutiveLossDays(String comm) {
        if (comm == null)  { return -1;}  //invalid commodity
        //exact match
        int commIndex = -1;
        for(int c=0; c< COMMS; c++){
            if (commodities[c].equals(comm)){
                commIndex=c;
                break;
            }
        }
        if (commIndex == -1){return -1;}
        int longestStreak=0;
        int currentStreak=0;
        // (across whole year)
        for (int m=0;m<MONTHS;m++){
            for (int d=0;d<DAYS;d++){
                if(profit[m][d][commIndex]<0){
                    currentStreak++;
                    if(currentStreak>longestStreak){
                        longestStreak=currentStreak;
                    }
                }else {
                    currentStreak = 0; //reset streak
                }
            }
        }
        return longestStreak;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        if (comm==null) {return -1;}   //invalid comm
        int commIndex=-1;
        for (int c=0; c<COMMS;c++){
            if (commodities[c].equals(comm)){
                commIndex=c;
                break; //when we find the commodity loop will end
            }
        }
        if (commIndex==-1)  {return -1;}  //invalid comm
        int count=0;
        for (int m=0;m<MONTHS;m++){   //checking all days
            for (int d=0;d<DAYS;d++){
                if (profit[m][d][commIndex]>threshold){
                    count++;
                }
            }
        }

        return count;
    }

    public static int biggestDailySwing(int month) {
        if (month<0 || month>=MONTHS){  //invalid month chech
            return  -99999;
        }
        int total=0;  //total profit on day 0
        for (int c=0;c<COMMS;c++){
        total+=profit[month][0][c];
        }
        int maxSwing=0;
        for (int d=1;d<DAYS;d++){   //compare consecutive days
            int nextTotal=0;
            for (int c=0;c<COMMS;c++){
             nextTotal+=profit[month][d][c] ;
            }
            int difference=nextTotal-total;
            if (difference<0) {
                difference = -difference; //absolute difference
            }
            if (difference>maxSwing){
                maxSwing=difference;
            }
            total=nextTotal; //move to the next day
            }
        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        return "DUMMY is better by 1234";
    }

    public static String bestWeekOfMonth(int month) {
        return "DUMMY";
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}
