package com.payparade.dataobject;

public class PPPartnerDomainDataObject {

	private int id;
	private String pp_partner_domain;
	private int pp_purchase_order_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPp_partner_domain() {
		return pp_partner_domain;
	}

	public void setPp_partner_domain(String ppPartnerDomain) {
		pp_partner_domain = ppPartnerDomain;
	}

	public int getPp_purchase_order_id() {
		return pp_purchase_order_id;
	}

	public void setPp_purchase_order_id(int ppPurchaseOrderId) {
		pp_purchase_order_id = ppPurchaseOrderId;
	}

}
