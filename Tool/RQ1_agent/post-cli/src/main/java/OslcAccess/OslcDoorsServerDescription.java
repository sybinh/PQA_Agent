/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public enum OslcDoorsServerDescription implements OslcServerDescriptionI {

    PRODUCTIVE(
            "DOORS-PRODUCTIVE", // DGS Productive Repository
            "https://rb-alm-05-p-dwa.de.bosch.com:8443",
            "https://rb-alm-05-p-dwa.de.bosch.com:8443/dwa/rm/dxl",
            "https://rb-alm-05-p-dwa.de.bosch.com:8443",
            "a8398802-9832-da98d29acf",
            "9SfFZdUENBkS90quxf1P",
            EnumSet.of(DxlFeature.GET_ELEMENT_BY_URL_LIST)),
    //            "17582995-6548-4972-1186-45285693", // alt
    //            "Wh@tk3y4DGS"),
//    ACCEPTANCE(
//            "DOORS-PRODUCTIVE", // DGS Productive Repository
//            "https://rb-alm-05-p-dwa.de.bosch.com:8443",
//            "https://rb-alm-05-p-dwa.de.bosch.com:8443/dwa/rm/dxl",
//            "https://rb-alm-05-p-dwa.de.bosch.com:8443",
//            "a8398802-9832-da98d29acf",
//            "9SfFZdUENBkS90quxf1P",
//            EnumSet.of(DxlFeature.GET_ELEMENT_BY_URL_LIST)),
    TEST(
            "DOORS-TEST", // DGS Test Repository
            "https://rb-alm-05-t-dwa.de.bosch.com:8443",
            "https://rb-alm-05-t-dwa.de.bosch.com:8443/dwa/rm/dxl",
            //            "https://fe0vmc0569.de.bosch.com:8443",
            //            "https://fe0vmc0569.de.bosch.com:8443",
            "https://rb-alm-05-t-dwa.de.bosch.com:8443",
            "12345678-1234-1234-1234-12345678",
            "scriptsecret",
            EnumSet.of(DxlFeature.GET_ELEMENT_BY_URL_LIST,
                    DxlFeature.GET_OBJECTS_OF_MODULE,
                    DxlFeature.GET_OBJECTS_OF_BASELINE));

    public enum DxlFeature {
        GET_ELEMENT_BY_URL_LIST,
        GET_OBJECTS_OF_MODULE,
        GET_OBJECTS_OF_BASELINE
    }

    final private String name;
    final private String oslcUrl;
    final private String dxlUrl;
    final private String openUrl;
    final private String consumerKey;
    final private String consumerSecret;
    final private EnumSet<DxlFeature> dxlFeatures;

    OslcDoorsServerDescription(String name, String oslcUrl, String dxlUrl, String openUrl, String consumerKey, String consumerSecret, EnumSet<DxlFeature> dxlFeatures) {
        this.name = name;
        this.dxlUrl = dxlUrl;
        this.oslcUrl = oslcUrl;
        this.openUrl = openUrl;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.dxlFeatures = dxlFeatures;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getOslcUrl() {
        return (oslcUrl);
    }

    @Override
    public String getDeleteUrl() {
        return (null);
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getOpenUrl() {
        return (openUrl);
    }

    public String getDxlUrl() {
        return (dxlUrl);
    }

    public boolean isDxlAvailable(DxlFeature dxlFeature) {
        assert (dxlFeature != null);
        return (this.dxlFeatures.contains(dxlFeature));
    }

    @Override
    public int getMaxRequestNumberPerSession() {
        return (-1);
    }

}
