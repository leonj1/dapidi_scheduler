package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.RunState;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AllowedJobStates {

    private Map<RunState, List<RunState>> allowed = Maps.newHashMap();

    public AllowedJobStates() {
        allowed.put(RunState.INACTIVE, new ArrayList<RunState>() {{
            add(RunState.ON_ICE);
            add(RunState.ON_HOLD);
        }});
        allowed.put(RunState.STARTED, new ArrayList<RunState>() {{
            add(RunState.TERMINATED);
        }});
        allowed.put(RunState.STARTING, new ArrayList<RunState>() {{
            add(RunState.TERMINATED);
        }});
        allowed.put(RunState.RUNNING, new ArrayList<RunState>() {{
            add(RunState.TERMINATED);
        }});
        allowed.put(RunState.SUCCESS, new ArrayList<RunState>() {{
            add(RunState.ON_HOLD);
            add(RunState.ON_ICE);
        }});
        allowed.put(RunState.TERMINATED, new ArrayList<RunState>() {{
            add(RunState.ON_HOLD);
            add(RunState.ON_ICE);
        }});
        allowed.put(RunState.ON_ICE, new ArrayList<RunState>() {{
            add(RunState.INACTIVE);
            add(RunState.ON_HOLD);
        }});
        allowed.put(RunState.ON_HOLD, new ArrayList<RunState>() {{
            add(RunState.INACTIVE);
            add(RunState.ON_ICE);
        }});
    }

    public boolean allowed(RunState source, RunState destination) {
        return this.allowed.get(source).contains(destination);
    }
}
