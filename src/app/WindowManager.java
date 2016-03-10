/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author jaspertomas
 */
public class WindowManager {
    public static final int LOGINFRAME=1;
    public static final int SERVERSETTINGFRAME=2;
    public static void initialize()
    {
        new LoginFrame();
        new ServerSettingFrame();
    
    }
    public static void open(Integer id)
    {
        //close all open windows
        LoginFrame.getInstance().setVisible(false);
        ServerSettingFrame.getInstance().setVisible(false);
        
        //open chosen window
        switch(id)
        {
            case LOGINFRAME:LoginFrame.getInstance().setVisible(true);break;
            case SERVERSETTINGFRAME:ServerSettingFrame.getInstance().setVisible(true);break;
        }
    }
}
