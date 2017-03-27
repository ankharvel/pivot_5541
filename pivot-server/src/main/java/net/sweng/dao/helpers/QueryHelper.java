package net.sweng.dao.helpers;

import net.sweng.domain.ReportParameters;

/**
 * Date on 2/25/17.
 */
public interface QueryHelper {

    String buildQuery(ReportParameters parameters);

    String[] getHeaders(ReportParameters parameters);

}
