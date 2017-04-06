package com.brandonhogan.liftscout.tests.repository.model;

import com.brandonhogan.liftscout.repository.model.CombinedRecordResult;
import com.brandonhogan.liftscout.repository.model.Record;
import com.brandonhogan.liftscout.repository.model.Rep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Brandon on 3/28/2017.
 * Description :
 */

@RunWith(MockitoJUnitRunner.class)
public class CombinedRecordResultModelUnitTest {

    @Test
    public void CombinedRecordModel_CorrectProperties_assertTrue() {

        CombinedRecordResult combinedRecordResult = new CombinedRecordResult();

        Rep rep = new Rep();
        List<Record> records = new ArrayList<>();
        records.add(new Record());

        combinedRecordResult.setRep(rep);
        combinedRecordResult.setRecords(records);

        assertThat(combinedRecordResult.getRep(), is(rep));
        assertThat(combinedRecordResult.getRecords(), is(records));
    }

}
