package com.payparade.service;

import java.util.List;

import com.payparade.dataobject.PPConnectionDataObject;
import com.payparade.dataobject.PPMemberNetworkDataObject;
import com.payparade.dataobject.PPMemberShipDataObject;
import com.payparade.dataobject.PPUserDataObject;

public interface AuthService {

	void insertintopp_member_network(
			PPMemberNetworkDataObject ppMemberNetworkDataObject);

	int getNetworkId(String network);

	public List<PPConnectionDataObject> setSelectfrom_pp_connection(
			int networkcodeid, String friendid);

	public List<PPUserDataObject> PPUserDataObjectList(String username,
			String password);

	public List<PPMemberNetworkDataObject> Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid(
			int networkcodeid, String membersocialnetworkid);

	public List<PPMemberShipDataObject> Select_from_ppmembership_where_partnerdomainid_memberid(
			int partnerdomainid, int memberid);

	public short getPartnerDomainId(String partnerdomainurl);

	public void update_ppmembership(
			PPMemberShipDataObject ppMemberShipDataObject);

	public void insertinto_ppmembership(
			PPMemberShipDataObject ppMemberShipDataObject);

	public void insertinto_pp_member(int partnerdomainid);

	public void insertinto_ppmembershipv1(
			PPMemberShipDataObject ppMemberShipDataObject);

	public void insertinto_ppmembernetwork(
			PPMemberNetworkDataObject ppMemberNetworkDataObject);

	public void update_ppmembershipv1(
			PPMemberShipDataObject ppMemberShipDataObject);

}
