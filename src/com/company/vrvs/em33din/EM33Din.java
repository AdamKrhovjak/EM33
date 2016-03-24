package com.company.vrvs.em33din;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * Created by Adam Krhovják on 11.3.2016.
 */
public class EM33Din {
    /**
     *@param portName
     */
    public static CommunicationChannel openCommunicationChannel(String portName)  throws Exception {
        CommunicationChannel em33DinCommunicationChannel = null;
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier( portName );
        if( portIdentifier.isCurrentlyOwned() ) {
            System.out.println( "Error: Port is currently in use" );
        } else {
            int timeout = 2000;
            CommPort commPort = portIdentifier.open(EM33Din.class.getName(), timeout );

            if( commPort instanceof SerialPort) {
                SerialPort serialPort = ( SerialPort )commPort;
                serialPort.setSerialPortParams( 9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE );
                em33DinCommunicationChannel = new CommunicationChannel(serialPort.getInputStream(), serialPort.getOutputStream());
                em33DinCommunicationChannel.start();
            } else {
                System.out.println( "Error: Only serial ports are handled by this example." );
            }
        }
        return em33DinCommunicationChannel;
    }
}
