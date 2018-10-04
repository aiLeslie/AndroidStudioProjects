package com.example.administrator.robredpacket;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class RedPacketService extends AccessibilityService {
    private String[] filter = {"恭喜发财","恭喜发财,大吉大利!"};
    private AccessibilityNodeInfo rootNodeInfo;

    public RedPacketService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        rootNodeInfo = event.getSource();
        if (rootNodeInfo == null) return;
        startClick(rootNodeInfo);
    }

    private void startClick(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list = findByText(rootNodeInfo);
        if (list == null)return;
        AccessibilityNodeInfo nodeInfo = list.get(list.size() - 1);
        if(nodeInfo != null){
            if("已拆开".equals(nodeInfo.getText()))return;


            boolean isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo rootNodeInfo) {
        for (String name : filter) {
            List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText(name);
            if (list != null && !list.isEmpty()) {
                return list;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {

    }


}
