package com.payparade.service;

import java.io.PrintWriter;
import java.util.List;

import com.payparade.dataobject.PPConnectionDataObject;
import com.payparade.dataobject.PPDashBoardDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;

public interface DataService {

	public void insertPPConnection(PPConnectionDataObject ppConnectionDataObject);

	public List<PPDashBoardDataObject> dashBoardDataObjectList(String sqlquery,
			final String subType);

	public void select_FROM_pp_ops_snapshot(final PrintWriter output);

	public int select_id_from_pp_partner_domain(String partnerdomain);

	public int select_id_from_pp_activity(String activity_name);

	public void select_FROM_pp_member_activiy(final String query,
			final PrintWriter output);

	public int select_id_from_pp_network(String networkcode);

	public void setSelect_distinct_social_id_friend_id_from_pp_connection(
			final int networkcodeid, final String socialNetworkid,
			final PrintWriter output);

	public void setSelect_logo_from_pp_partneroption(final int networkcodeid,
			final PrintWriter output);

	public void setSelect_from_pp_partnernetwork(final int partnerdomainid,
			final PrintWriter output);

	public void select_from_pp_partner_network_where_partner_domain_idand_network_code_id(
			final int partnerdomainid, final int networkcodeid,
			final PrintWriter output);

	public void select_from_pp_partner_options_WHERE_partner_domain(
			final int partnerdomainid, final PrintWriter output);

	public void select_from_pp_partner_options_where_usermail(
			final String usermail, final PrintWriter output);

	public void select_from_ppcustomer_rank_wherepartnerdomain_id(
			final int partnerdomainid, final PrintWriter output);

	public void select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude(
			final int partnerdomainid, final PrintWriter output,
			final double... latitude_longitude);

	public void updatedynamictable(String sql);

	public List<PPMemberActivityDataObject> select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity(
			String query);

	public int SELECT_count_val_from_pp_membership(String sqlquery);

	public List<PPMemberActivityDataObject> select_distinctmembersession_id_val_FROM_pp_member_activity(
			String query);
}
