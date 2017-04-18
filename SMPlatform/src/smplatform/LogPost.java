/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

/**
 *Creates a formatted text post to write to the log
 * @author edwardhein
 */
public class LogPost 
{
    private String name;
    private String content;
    private String date;
    
    public LogPost(String name, String content, String date)
    {
        this.name = name;
        this.content = content;
        this.date = date;
    }
    
    public String getName()
    {
        return name;
    }
    public String getContent()
    {
        return content;
    }
    public String getDate()
    {
        return date;
    }
    
    public String toString()
    {
        String ret = "|" + getDate() + "|" + getName() + ": " + getContent() + "|";
        return ret;
    }
    
    public String bodyToString()
    {
        String ret = "|" + getContent() + "|";
        return ret;
    }
}
