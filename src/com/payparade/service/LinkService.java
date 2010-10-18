package com.payparade.service;

import java.io.PrintWriter;
import java.util.List;

import com.payparade.dataobject.PPShareLinkDataObject;

public interface LinkService {

	public List<PPShareLinkDataObject> setSelect_ppsharedlink_where_shared_id(
			String sid);

	public int select_id_from_pp_partner_domain(String partnerdomain);

	public short getNetworkId(final String networkurl);

	public void Insert_into_ppshared_activity(String sid, String id);

	public int insert_pp_shared_link(
			final PPShareLinkDataObject ppShareLinkDataObject);

	public List<PPShareLinkDataObject> setSelect_ppsharedlink_where_shared_id(
			String sid, PrintWriter out);

	public List<PPShareLinkDataObject> setSelect_ppsharedlink_where_ppid(
			String ppid);
}
