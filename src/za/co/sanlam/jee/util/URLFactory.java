/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.sanlam.jee.util;

import java.net.URL;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.spi.ObjectFactory;

public class URLFactory implements ObjectFactory {

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
        Reference ref = (Reference) obj;
        String urlString = (String) ref.get("url").getContent();
        return new URL(urlString);
    }
}
