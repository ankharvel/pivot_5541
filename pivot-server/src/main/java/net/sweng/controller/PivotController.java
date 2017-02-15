package net.sweng.controller;

import net.sweng.dao.PivotDao;
import net.sweng.domain.TableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Date on 2/13/17.
 */
@Controller
public class PivotController {

    @Autowired
    private PivotDao pivotDao;

    public TableData generate(String sourcePath) {
        return pivotDao.getRecordsFromCsv(sourcePath);
    }

}
