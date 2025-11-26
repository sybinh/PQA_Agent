/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author hfi5wi
 */
public class Rq1XmlTableColumn_SuperOplKey extends Rq1XmlTableColumn_SecretString {
    
    final private static Logger LOGGER = Logger.getLogger(Rq1XmlTableColumn_SuperOplKey.class.getCanonicalName());
    final private SecretKeys key = SecretKeys.SUPER_OPL_KEY;
    
    public Rq1XmlTableColumn_SuperOplKey(String uiName, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, sourceName, encodingMethod);
    }
    
    public Rq1XmlTableColumn_SuperOplKey(String uiName, int columnWidth, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth, encodingMethod);
    }

    public Rq1XmlTableColumn_SuperOplKey(String uiName, int columnWidth, String sourceName, Rq1XmlTable.ColumnEncodingMethod encodingMethod) {
        super(uiName, columnWidth, sourceName, encodingMethod);
    }

    @Override
    public String getKey() {
        return key.getKey();
    }
    
    @Override
    public String encodeString(String rawString) {
        String encodedContent;
        try {
            byte[] keyArray = getKey().getBytes();
            Key aesKey = new SecretKeySpec(keyArray, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encodedBytes = cipher.doFinal(rawString.getBytes());
            encodedContent = Base64.getEncoder().encodeToString(encodedBytes);
            return encodedContent;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
                | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1XmlTableColumn_SuperOplKey.class.getCanonicalName(), ex);
            EcvUserMessage.showMessageDialog(
                    "Error occured during encoding " + getUiName() + "\n\n"
                    + ex,
                    "Error", EcvUserMessage.MessageType.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public String decodeString(String encodedString) {
        String decodedContent;
        try {
            byte[] keyArray = getKey().getBytes();
            Key aesKey = new SecretKeySpec(keyArray, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decodedBytes = cipher.doFinal(Base64.getDecoder().decode(encodedString));
            decodedContent = new String(decodedBytes);
            return decodedContent;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
                | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            EcvUserMessage.showMessageDialog(
                    "Error occured during decoding " + getUiName() + "\n\n"
                    + ex,
                    "Error", EcvUserMessage.MessageType.ERROR_MESSAGE);
            ToolUsageLogger.logError(Rq1XmlTableColumn_SuperOplKey.class.getCanonicalName(), ex);
            return null;
        }
    }
}
