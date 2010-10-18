package com.payparade.service;

import java.util.List;

import com.payparade.dataobject.PPCustomerScoreDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;

public interface BatchService {

	public boolean delete_customer_score();

	public boolean delete_customer_rank();

	public boolean delete_last_access();

	public List<PPMemberActivityDataObject> getsocial_id_networkcodeid_maxppidfrom_pp_member_activity();

	public void update_pp_id_of_pp_member_activity(
			List<PPMemberActivityDataObject> memberActivityDataObjectList);

	public void insert_pp_customer_score(
			List<PPCustomerScoreDataObject> memberActivityDataObjectList);

	public void select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity(
			String fromdate);

	public void update_pp_customer_score(
			List<PPMemberActivityDataObject> ppmemberActivityDataObjectList);

	public void select_ppid_partnerdomainid_getcount_pp_member_activity(
			String fromdate);

	public void select_member_id_member_social_network_id_max_friends_from_pp_member_network();

	public void update_pp_customer_score_v1(
			List<PPMemberActivityDataObject> memberActivityDataObjectList);

	public void select_ppid_partnerdomainid_countswf(String fromdate);

	public void select_ppid_partnerdomainid_countfct(String fromdate);

	public void update_pp_customer_score_v3(
			List<PPMemberActivityDataObject> memberActivityDataObjectList);

	public List<PPCustomerScoreDataObject> select_vts_qov_son_swf_from_ppcustomerscore();

	public void insert_ppcustomerrank(int affinityMax, int influenceMax);

	public void insert_pplastaccess();

	public void update_ppcustomerrank();

	public void update_ppcustomerrankv1();

	public int select_id_from_pp_partner_domain(String partnerdomain);

}
