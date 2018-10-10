package com.rahul.locationalarm.common;

import java.util.List;

public interface DBTaskFetchCompletionListener {

    void onDBTaskCompleted(List<?> items);

}
