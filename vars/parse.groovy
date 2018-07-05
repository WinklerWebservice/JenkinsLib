import groovy.util.XmlSlurper
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/*
    Convert unixtimestamp to formatted date in yyyy-MM-dd HH:mm:ss

    Vars: INT
    Return: String
*/

@NonCPS
def XML(response){
    def data = new XmlSlurper().parseText(response)
    return data.children().children()
}

def JSON(response){
    return new JsonSlurper().parseText(response)  
}