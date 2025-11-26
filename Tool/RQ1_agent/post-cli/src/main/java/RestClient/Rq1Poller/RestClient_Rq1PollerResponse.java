/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Rq1Poller;

import java.util.Objects;

/**
 *
 * @author hfi5wi
 */
public class RestClient_Rq1PollerResponse {

    final private boolean sutitableForINMA;
    final private String inmaComment;

    public RestClient_Rq1PollerResponse(boolean sutitableForINMA, String inmaComment) {
        assert (inmaComment != null);
        assert (inmaComment.isEmpty() == false);

        this.sutitableForINMA = sutitableForINMA;
        this.inmaComment = inmaComment;
    }

    public boolean isSutitableForINMA() {
        return (sutitableForINMA);
    }

    public String getInmaComment() {
        return (inmaComment);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RestClient_Rq1PollerResponse) {
            RestClient_Rq1PollerResponse response = (RestClient_Rq1PollerResponse) obj;
            return (response.isSutitableForINMA() == this.sutitableForINMA && response.getInmaComment().equals(this.inmaComment));
        } else {
            return (false);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.sutitableForINMA ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.inmaComment);
        return hash;
    }

    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + ": " + sutitableForINMA + " " + inmaComment);
    }

}
