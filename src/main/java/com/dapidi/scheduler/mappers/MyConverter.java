package com.dapidi.scheduler.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface MyConverter {
    Object object(ResultSet rs) throws SQLException, ParseException;
    String name();
}
