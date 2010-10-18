package com.payparade.service;

import java.util.List;

import com.payparade.dataobject.PPAdminMailDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPOpsSnapShotDataObject;
import com.payparade.dataobject.PPPartnerMemberActivityDataObject;

public interface AdminService {

	public PPAdminMailDataObject select_pp_admin_mail();

	public void getOpGraph(final String[] parts);

	public int select_id_from_pp_partner_domain(String partnerdomain);

	public void insert_into_pp_ops_snapshot(
			PPOpsSnapShotDataObject ppOpsSnapShotDataObject);

	public List<PPMemberActivityDataObject> select_dt_con_opp_from_pp_membership_ppmemberactivity(
			int partnerid, String query);

	public List<PPPartnerMemberActivityDataObject> select_from_pp_partner_member_activity(
			String query);

	public List<PPOpsSnapShotDataObject> select_reportfieldsfrom_pp_ops_snapshort(
			String query);

}
