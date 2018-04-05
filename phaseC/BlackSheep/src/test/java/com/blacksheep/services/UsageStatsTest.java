/**
 * Author : Athul Muralidharan
 * Created at : 3 30 am ,Apr 5 2018
 * Version 1
 * Tests for Usage stats
 */

package com.blacksheep.services;

import com.blacksheep.ErrorCodes;
import org.junit.Test;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

public class UsageStatsTest {

    @Test
    public void usageStats() {

        // if any of the exception  is picked up,
        // it means usageStats is unreachable()
        //  Hence it should fail

        boolean testSuccess = true;
        try {
            UsageStats.usageStats();
        } catch (SQLException e) {
            testSuccess = false;
            e.printStackTrace();
        } catch (IOException e) {
            testSuccess = false;
            e.printStackTrace();
        }


        assert testSuccess;

    }

    /**Test for if increment usage count is successful and decrement is successful
     *
     */
    @Test
    public void incrementDecrementUsageCount() throws IOException, SQLException {

        assertEquals(ErrorCodes.SUCCESS, UsageStats.incrementUsageCount());
        assertEquals(ErrorCodes.SUCCESS, UsageStats.decrementUsageCount());

    }


}