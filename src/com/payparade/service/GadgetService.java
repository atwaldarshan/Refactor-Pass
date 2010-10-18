package com.payparade.service;

import java.util.List;
import java.util.Map;

import com.payparade.dataobject.PPMessagesDataObject;
import com.payparade.dataobject.PPOpsSnapShotDataObject;
import com.payparade.dataobject.PPShareLinkDataObject;

public interface GadgetService {

	public void initializeGadgetList();

	public Map<String, String> getPartnerCSS_();

	public Map<String, String> getConnectSize_();

	public Map<String, String> getConnectText_();

	public Map<String, String> getMySpaceApps_();

	public int select_id_from_pp_partner_domain(String partnerdomain);

	public int insert_pp_shared_link(PPShareLinkDataObject ppShareLinkDataObject);

	public int getNetworkId(String networkcode);

	public List<PPMessagesDataObject> select_pp_messages();

	public List<PPOpsSnapShotDataObject> select_numuser_from_pp_ops_snapshot();

	public List<PPOpsSnapShotDataObject> select_numofrequest_from_pp_ops_snapshot();

	public List<PPOpsSnapShotDataObject> select_totalmills_from_pp_ops_snapshot();

}
