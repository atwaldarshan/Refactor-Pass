package com.payparade.service;

import java.util.Map;

import com.payparade.dataobject.PPIdDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPMemberSessionDataObject;
import com.payparade.dataobject.PPPartnerOptionDataObject;
import com.payparade.dataobject.PPPartnerServerDataObject;

public interface PassService {

	public void insertPPMemberActivity(
			PPMemberActivityDataObject ppMemberActivityDataObject);

	public long insertPPId(PPIdDataObject ppIdDataObject);

	public short getNetworkIdUsingNetworkcodeid(String networkurl);

	public short getNetworkIdUsingURL(String networkurl);

	public short getPartnerDomainId(String partnerdomainurl);

	public Map<Integer, PPPartnerOptionDataObject> select_pp_partner_option();

	public String Select_payparade_server_from_pp_partner_server(
			String partnercode);

	public Map<String, PPPartnerServerDataObject> select_from_pp_partner_servers();

	public void insert_into_pp_memeber_session(
			PPMemberSessionDataObject ppMemberSessionDataObject);

}
