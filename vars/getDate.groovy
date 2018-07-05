import java.text.SimpleDateFormat

/*
    Convert unixtimestamp to formatted date in yyyy-MM-dd HH:mm:ss

    Vars: INT
    Return: String
*/

def FromSeconds(unix){
    Date date = new Date(((long)unix)*1000); 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    String formattedDate = sdf.format(date);
    return formattedDate
}

def FromMilliSeconds(unix){
    Date date = new Date(((long)unix)); 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    String formattedDate = sdf.format(date);
    return formattedDate
}