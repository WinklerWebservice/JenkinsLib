package de.qsc

class Kix {
    String host
    String userlogin
    String password

    Kix(mainurl, user, pass){
        this.host = mainurl
        this.userlogin = user
        this.password = pass
    }
    
    /*
        Get all Tickets from KIX
        Required Variables
    */
    public String getAllTickets(){
        getAllTickets([:])
    }

    public String getAllTickets(LinkedHashMap filter, debug = false){
        def xml = ''
        
        filter.each { k, n ->
            if(n instanceof java.util.ArrayList){
                n.each{ v -> xml = xml + '<' + k + '>' + v + '</' + k + '>'; }
            } else {
                xml = xml + '<' + k + '>' + n + '</' + k + '>'
            }           
        }

        return runRequest(xml, 'TicketSearch', debug)
    }
    
    /*
        Get Ticket from KIX
        Required variables:
        - Ticket_ID (INT)
        Optional variables:
        - dynamicFields (Boolean)
        - history (Boolean)
        - order ('ASC', 'DESC')
    */
    /// <summary>
    /// Get a single ticket from KIX with given ID. For minimal output dynamicFields and history are set false by default. Articlerorder is DESC (Last in, First out) by default.
    /// </summar>
    public String getTicket(ID) throws IOException {     
        getTicket(ID, false, false, 'DESC')    
    }
    public String getTicket(ID, dynamicFields) throws IOException {
        getTicket(ID, dynamicFields, false, 'DESC')
    }
    public String getTicket(ID, dynamicFields, history) throws IOException {
        getTicket(ID, dynamicFields, history, 'DESC')
    }    
    public String getTicket(ID, dynamicFields, history, order) throws IOException {        
        def xml = ''

        if(ID){
            xml = xml + '<TicketID>' + ID + '</TicketID>'
        } else {
            return '<soap><body><Error><ErrorMessage>Missing Ticket_ID</ErrorMessage></Error></body></soap>'
        }        
                  
        if(dynamicFields){
            xml = xml + '<DynamicFields>1</DynamicFields>'
        }
        
        if(history){
            xml = xml + '<AllArticles>1</AllArticles>'
        }
        
        if(order){
            if (order.toLowerCase() == 'asc' || order.toLowerCase() == 'desc' ) {
                xml = xml + '<ArticleOrder>' + order.toUpperCase() + '</ArticleOrder>'
            } else {
                xml = xml + '<ArticleOrder>DESC</ArticleOrder>'
             }
        }

        return runRequest(xml, 'TicketGet')       
    }
    
    /*
        Creates a ticket in KIX
        Required variables:
        - 
    */
    public String createNewTicket(data) throws IOException {        
        def ticketdata = ''
        def articledata = ''

        if (!data.containsKey('Title')){
            return '<soap><body><Error><ErrorMessage>Missing Title</ErrorMessage></Error></body></soap>'
        } else {
            ticketdata = ticketdata + '<Title>' + data['Title'] + '</Title>'
        }
        if (!data.containsKey('QueueID')){
            return '<soap><body><Error><ErrorMessage>Missing QueueID</ErrorMessage></Error></body></soap>'
        } else {
            ticketdata = ticketdata + '<QueueID>' + data['QueueID'] + '</QueueID>'
        }
        if (!data.containsKey('Lock')){
            ticketdata = ticketdata +  '<Lock>unlock</Lock>'
        } else {
            ticketdata = ticketdata + '<Lock>' + data['Lock'] + '</Lock>'
        }
        if (!data.containsKey('PriorityID')){
            ticketdata = ticketdata +  '<PriorityID>1</PriorityID>'
        } else {
            ticketdata = ticketdata + '<PriorityID>' + data['PriorityID'] + '</PriorityID>'
        }
        if (!data.containsKey('StateID')){
            ticketdata = ticketdata + '<StateID>1</StateID>'
        } else {
            ticketdata = ticketdata + '<StateID>' + data['StateID'] + '</StateID>'
        }        
        if (!data.containsKey('CustomerID')){
            return '<soap><body><Error><ErrorMessage>Missing CustomerID</ErrorMessage></Error></body></soap>'
        } else {
            ticketdata = ticketdata + '<CustomerID>' + data['CustomerID'] + '</CustomerID>'
        }
        if (!data.containsKey('CustomerUser')){
            return '<soap><body><Error><ErrorMessage>Missing CustomerUser</ErrorMessage></Error></body></soap>'
        } else {
            ticketdata = ticketdata + '<CustomerUser>' + data['CustomerUser'] + '</CustomerUser>'
        } 
        if (!data.containsKey('OwnerID')){
            ticketdata = ticketdata + '<OwnerID>1</OwnerID>'
        } else {
            ticketdata = ticketdata + '<OwnerID>' + data['OwnerID'] + '</OwnerID>'
        }
        if (!data.containsKey('TypeID')){
            ticketdata = ticketdata + '<TypeID>1</TypeID>'
        } else {
            ticketdata = ticketdata + '<TypeID>' + data['TypeID'] + '</TypeID>'
        }        
        
        if (!data.containsKey('ArticleType')){
            articledata = articledata + '<ArticleType>phone</ArticleType>'
        } else {
            articledata = articledata + '<ArticleType>' + data['ArticleType'] + '</ArticleType>'
        }
        if (!data.containsKey('SenderType')){
            articledata = articledata + '<SenderType>system</SenderType>'
        } else {
            articledata = articledata + '<SenderType>' + data['SenderType'] + '</SenderType>'
        } 
        if (!data.containsKey('ContentType')){
            articledata = articledata + '<ContentType>text/plain; charset=ISO-8859-15</ContentType>'
        } else {
            articledata = articledata + '<ContentType>' + data['ContentType'] + '</ContentType>'
        }   
        if (!data.containsKey('Subject')){
            articledata = articledata + '<Subject>Ticket von Remedey</Subject>'
        } else {
            articledata = articledata + '<Subject>' + data['Subject'] + '</Subject>'
        }
        if (!data.containsKey('Body')){
            articledata = articledata + '<Body>Hier ist etwas schief gelaufen. Scheinbar wurde der Text nicht übernommen</Body>'
        } else {
            articledata = articledata + '<Body>' + data['Body'] + '</Body>'
        }  
        if (!data.containsKey('HistoryType')){
            articledata = articledata + '<HistoryType>NewTicket</HistoryType>'
        } else {
            articledata = articledata + '<HistoryType>' + data['HistoryType'] + '</HistoryType>'
        }
        if (!data.containsKey('HistoryComment')){
            articledata = articledata + '<HistoryComment>Create new Ticket from Remedy</HistoryComment>'
        } else {
            articledata = articledata + '<HistoryComment>' + data['HistoryComment'] + '</HistoryComment>'
        }        

        def xml = '<Ticket>' + ticketdata + '</Ticket><Article>' + articledata + '</Article>'
        
        return runRequest(xml, 'TicketCreate')       
    }
    
    /*
        Update Ticket in KIX
        Required variables:
        - 
        
    */
    public String updateTicket(data) throws IOException {     
        def ticketdata = ''
        def articledata = ''
        def xml = ''

        if (!data.containsKey('TicketID')){
            return '<soap><body><Error><ErrorMessage>Missing Ticket_ID</ErrorMessage></Error></body></soap>'
        } else {
            xml = xml + '<TicketID>' + data['TicketID'] + '</TicketID>'
        }
        
        if (data.containsKey('Title')){
            ticketdata = ticketdata + '<Title>' + data['Title'] + '</Title>'
        }
        if (data.containsKey('QueueID')){
            ticketdata = ticketdata + '<QueueID>' + data['QueueID'] + '</QueueID>'
        }
        if (data.containsKey('Lock')){
            ticketdata = ticketdata + '<Lock>' + data['Lock'] + '</Lock>'
        }
        if (data.containsKey('PriorityID')){
            ticketdata = ticketdata + '<PriorityID>' + data['PriorityID'] + '</PriorityID>'
        }
        if (data.containsKey('StateID')){
            ticketdata = ticketdata + '<StateID>' + data['StateID'] + '</StateID>'
        }        
        if (data.containsKey('CustomerUser')){
            ticketdata = ticketdata + '<CustomerUser>' + data['CustomerUser'] + '</CustomerUser>'
        } 
        if (data.containsKey('OwnerID')){
            ticketdata = ticketdata + '<OwnerID>' + data['OwnerID'] + '</OwnerID>'
        }       
        
        if (!data.containsKey('ArticleType')){
            articledata = articledata + '<ArticleType>phone</ArticleType>'
        } else {
            articledata = articledata + '<ArticleType>' + data['ArticleType'] + '</ArticleType>'
        }
        if (!data.containsKey('SenderType')){
            articledata = articledata + '<SenderType>system</SenderType>'
        } else {
            articledata = articledata + '<SenderType>' + data['SenderType'] + '</SenderType>'
        } 
        if (!data.containsKey('ContentType')){
            articledata = articledata + '<ContentType>text/plain; charset=ISO-8859-15</ContentType>'
        } else {
            articledata = articledata + '<ContentType>' + data['ContentType'] + '</ContentType>'
        }   
        if (!data.containsKey('Subject')){
            articledata = articledata + '<Subject>Update von Remedey</Subject>'
        } else {
            articledata = articledata + '<Subject>' + data['Subject'] + '</Subject>'
        }
        if (!data.containsKey('Body')){
            articledata = articledata + '<Body>Hier ist etwas schief gelaufen. Scheinbar wurde der Text nicht übernommen</Body>'
        } else {
            articledata = articledata + '<Body>' + data['Body'] + '</Body>'
        }  
        if (!data.containsKey('HistoryType')){
            articledata = articledata + '<HistoryType>AddNote</HistoryType>'
        } else {
            articledata = articledata + '<HistoryType>' + data['HistoryType'] + '</HistoryType>'
        }
        if (!data.containsKey('HistoryComment')){
            articledata = articledata + '<HistoryComment>Get information from Remedy</HistoryComment>'
        } else {
            articledata = articledata + '<HistoryComment>' + data['HistoryComment'] + '</HistoryComment>'
        }        

        if(ticketdata != ''){
            xml = xml + '<Ticket>' + ticketdata + '</Ticket>'
        }
        
        if(articledata != ''){
            xml = xml + '<Article>' + articledata + '</Article>'
        }
        
        return runRequest(xml, 'TicketUpdate')
    }

    /*

    */
    public getStateTyp(StateID){
        switch(StateID){
            case 1:
            return ''
            case 2:
            return ''
            case 3: ''
            return
            default:
            return ''
        }
    }

    private runRequest(xml, soapaction, debug = false) throws IOException {
        def fullxml = '<?xml version="1.0" encoding="UTF-8"?>' + 
                    '<soap:Envelope soap:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">' +
                    '<soap:Body><' + soapaction + ' xmlns="http://www.otrs.org/TicketConnector/">' +
                    '<UserLogin>' + this.userlogin + '</UserLogin>' + 
                    '<Password>' + this.password + '</Password>' + 
                    xml + '</' + soapaction + '></soap:Body></soap:Envelope>'
        if(debug){
            return fullxml
        }      
        URL url = new URL(this.host);
        HttpURLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty( "Content-Type", "text/xml" );
        conn.setRequestProperty( "Content-Type", "charset=utf-8"); 
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(fullxml);
        wr.close();

        // Get the response
        String response;
        InputStream responseStream;
        try {
            responseStream = conn.getInputStream();
        } catch( IOException e ) {
            if( conn.getResponseCode() == 500 ) {
                responseStream = conn.getErrorStream();
            } else throw e;
        }
        response = responseStream.getText("utf-8");
        responseStream.close();

        return response.trim().replaceFirst("^([\\W]+)<","<");       
    }
}