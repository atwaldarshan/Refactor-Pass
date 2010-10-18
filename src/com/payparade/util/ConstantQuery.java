package com.payparade.util;

public class ConstantQuery {

	public static final String insert_pp_member_activity = "Insert into pp_member_activity (member_social_network_id,last_time_stamp,member_session_id,"
			+ " network_code_id,activity_code_id,page_url,target_url,partner_domain_id,idx,pp_id)"
			+ " values(?,now(),?,?,?,?,?,?,?,?)";

	public static final String insert_pp_id = "Insert into pp_id(first_seen,session_id,browser_id,reference_url,user_ip)"
			+ "values(now(),?,?,?,?)";

	public static final String insert_pp_connection = "INSERT INTO pp_connection (network_code_id,member_social_network_id,friend_id,current_time_stamp)"
			+ "values(?,?,?,now())";

	public static final String delete_pp_customer_score = "delete from pp_customer_score";

	public static final String delete_pp_customer_rank = "delete from pp_customer_rank";

	public static final String delete_pp_last_access = "delete from pp_last_access";

	public static final String select_social_id_networkcodeid_maxppidfrom_pp_member_activity = "select member_social_network_id,network_code_id,"
			+ " max(CONVERT(pp_id, UNSIGNED))max_id from pp_member_activity where pp_id!=0 and member_social_network_id!='' and network_code_id!=0 group by member_social_network_id,network_code_id";

	public static final String update_pp_id_of_pp_member_activity = "UPDATE pp_member_activity SET pp_id =? WHERE member_social_network_id=? AND network_code_id=?";

	public static final String select_ppid_membersocialid_network_code_id_partnerdomainid_vts_qov_from_pp_member_activity = "SELECT pp_id, member_social_network_id, network_code_id, partner_domain_id, count(distinct member_session_id) vts, count(page_url)/count(distinct member_session_id) qov from pp_member_activity a"
			+ " WHERE last_time_stamp > ? AND pp_id != '' AND partner_domain_id != '' AND network_code_id != ''  "
			+ " AND pp_id != 0 AND partner_domain_id != 0 AND network_code_id != 0 GROUP BY pp_id, partner_domain_id ";

	public static final String Insert_pp_customer_score = " Insert into pp_customer_score (pp_id, network_code_id,network_member_social_id,partner_domain_id, vts, qov ) VALUES (?,?,?,?,?,?) ";

	public static final String select_ppid_partnerdomainid_countvaluefrom_ppmember_activity = "SELECT pp_id, partner_domain_id, count(*) sio from pp_member_activity   WHERE activity_code_id =6 and last_time_stamp > ? and pp_id!=0 and pp_id!='' and partner_domain_id!=0"
			+ "partner_domain_id!='' GROUP BY pp_id, partner_domain_id";

	public static final String pp_customer_score_SET_sio = " update pp_customer_score SET sio=?  WHERE partner_domain_id = ? AND pp_id=? ";

	/*
	 * public static final String
	 * select_member_id_member_social_network_id_max_friends_from_pp_member_network
	 * =
	 * " select member_id,member_social_network_id,network_code_id, max(friends) son FROM ( "
	 * +
	 * "SELECT n.member_id, n.member_social_network_id, network_code_id, p.time_stamp, count(*) friends FROM pp_connection p, pp_member_network n"
	 * ;
	 */
	public static final StringBuffer select_member_id_member_social_network_id_max_friends_from_pp_member_network = new StringBuffer();

	public static String getSelect_member_id_member_social_network_id_max_friends_from_pp_member_network() {
		select_member_id_member_social_network_id_max_friends_from_pp_member_network
				.append("select member_social_network_id,network_code_id, max(friends) son FROM (");
		select_member_id_member_social_network_id_max_friends_from_pp_member_network
				.append(" SELECT n.member_id, n.member_social_network_id, n.network_code_id, p.current_time_stamp, count(*) friends FROM pp_connection p, pp_member_network n");
		select_member_id_member_social_network_id_max_friends_from_pp_member_network
				.append(" WHERE p.network_code_id = n.network_code_id and n.member_social_network_id = p.member_social_network_id");
		select_member_id_member_social_network_id_max_friends_from_pp_member_network
				.append(" GROUP BY n.member_id, n.member_social_network_id, n.network_code_id, p.current_time_stamp ) a ");
		select_member_id_member_social_network_id_max_friends_from_pp_member_network
				.append(" GROUP BY member_id, member_social_network_id, network_code_id ");
		return select_member_id_member_social_network_id_max_friends_from_pp_member_network
				.toString();
	}

	public static final StringBuffer update_pp_customer_scorev1 = new StringBuffer();

	public static StringBuffer getUpdatePpCustomerScorev1() {
		update_pp_customer_scorev1
				.append(" UPDATE pp_customer_score SET son = ? where network_member_social_id = ? and network_code_id = ? ");
		return update_pp_customer_scorev1;
	}

	public static final StringBuffer select_ppid_partnerdomainid_countswf = new StringBuffer();

	public static StringBuffer getSelectPpidPartnerdomainidCountswf() {
		select_ppid_partnerdomainid_countswf
				.append(" SELECT pp_id,  count(*) swf from pp_member_activity ");
		select_ppid_partnerdomainid_countswf
				.append(" WHERE last_time_stamp > ? and pp_id!=0 and activity_code_id=7 and partner_domain_id!=0 GROUP BY pp_id, partner_domain_id");
		return select_ppid_partnerdomainid_countswf;
	}

	public static final StringBuffer update_pp_customer_scorev2 = new StringBuffer();

	public static StringBuffer getUpdatePpCustomerScorev2() {
		update_pp_customer_scorev2
				.append(" UPDATE pp_customer_score SET swf = ? where pp_id = ? ");
		return update_pp_customer_scorev2;
	}

	public static final StringBuffer select_ppid_partnerdomainid_countfct = new StringBuffer();

	public static StringBuffer getSelectPpidPartnerdomainidCountfct() {
		select_ppid_partnerdomainid_countfct
				.append(" SELECT pp_id,  count(*) fct from pp_member_activity ");
		select_ppid_partnerdomainid_countfct
				.append(" WHERE last_time_stamp > ? and pp_id!=null and activity_code_id=8 and partner_domain_id!=0 GROUP BY pp_id, partner_domain_id");
		return select_ppid_partnerdomainid_countfct;
	}

	public static final StringBuffer update_pp_customer_scorev3 = new StringBuffer();

	public static StringBuffer getUpdatePpCustomerScorev3() {
		update_pp_customer_scorev3
				.append(" UPDATE pp_customer_score SET fct = ? where pp_id = ? ");
		return update_pp_customer_scorev3;
	}

	// public static final StringBuffer select_maxvts_
	// maxqov_maxson_maxswf_from_ppcustomerscore = new StringBuffer();
	public static final StringBuffer select_vts_qov_son_swf_from_ppcustomerscore = new StringBuffer();

	public static String getSelectVtsQovSonSwfFromPpcustomerscore() {
		select_vts_qov_son_swf_from_ppcustomerscore
				.append("SELECT max(vts) max_vts, max(qov) max_qov, max(son) max_son, max(swf) max_swf FROM pp_customer_score");
		return select_vts_qov_son_swf_from_ppcustomerscore.toString();
	}

	public static final StringBuffer insert_pp_customer_rank = new StringBuffer();

	public static StringBuffer getInsertPpCustomerRank() {
		insert_pp_customer_rank
				.append("INSERT INTO pp_customer_rank ( pp_id, partner_domain_id,network_code_id,network_member_social_id, brand_affinity, influence, friends )");
		insert_pp_customer_rank
				.append(" select pp_id,partner_domain_id,network_code_id,network_member_social_id,100+((300*IFNULL(vts,0)*IFNULL(qov,0))/?), 100+((300*IFNULL(son,0)*IFNULL(swf,0))/?), son   FROM pp_customer_score  ");
		return insert_pp_customer_rank;
	}

	public static final StringBuffer insert_into_pplastaccess = new StringBuffer();

	public static StringBuffer getInsertIntoPplastaccess() {
		insert_into_pplastaccess
				.append("INSERT INTO pp_last_access( pp_id, partner_domain_id, last_access ) SELECT pp_id, partner_domain_id, max(last_time_stamp) FROM pp_member_activity where partner_domain_id !=0 GROUP BY pp_id, partner_domain_id ");
		return insert_into_pplastaccess;
	}

	public static final StringBuffer update_pp_customer_rank = new StringBuffer();

	public static StringBuffer getUpdate_pp_customer_rank() {
		update_pp_customer_rank
				.append(" UPDATE pp_customer_rank r SET last_seen = ( SELECT last_access FROM pp_last_access a WHERE a.pp_id = r.pp_id AND a.partner_domain_id = r.partner_domain_id) ");
		return update_pp_customer_rank;
	}

	public static final StringBuffer update_pp_customer_rankv1 = new StringBuffer();

	public static StringBuffer getUpdatePpCustomerRankv1() {
		update_pp_customer_rankv1
				.append(" UPDATE pp_customer_rank r, pp_ip ip, pp_id id SET r.latitude = CONVERT(ip.latitude, DECIMAL(9,6)), r.longitude = CONVERT(ip.longitude, DECIMAL(9,6))  WHERE id.pp_id = r.pp_id AND id.user_ip = ip.ip_address ");
		return update_pp_customer_rankv1;
	}

	/******************************** AuthController Start *************************************************/

	public static final StringBuffer insert_ppmembernetwork = new StringBuffer();

	public static String getInsertPpmembernetwork() {

		insert_ppmembernetwork
				.append("Insert into pp_member_network(network_code_id,member_social_network_id,connected_at) ");
		insert_ppmembernetwork.append(" values(?,?,now()) ");

		return insert_ppmembernetwork.toString();
	}

	public static final StringBuffer select_id_ppnetwork = new StringBuffer();

	public static StringBuffer getSelectIdPpnetwork() {
		select_id_ppnetwork
				.append(" select id from pp_network where network_code=?");
		return select_id_ppnetwork;
	}

	public static StringBuffer listAllSigningSecret = new StringBuffer();

	public static StringBuffer getListAllSigningSecret() {
		listAllSigningSecret.append("select * from pp_signing_secret");
		return listAllSigningSecret;
	}

	public static StringBuffer selectidfrompppdomain = new StringBuffer();

	public static StringBuffer getSectidfrompppdomain() {
		selectidfrompppdomain
				.append(" Select id from pp_domain where partner_domain=?");
		return selectidfrompppdomain;
	}

	public static StringBuffer selectidfrompppnetwork_url = new StringBuffer();

	public static StringBuffer getSelectidfrompppnetwork_url() {
		selectidfrompppnetwork_url
				.append("select id from pp_network where reference_url=?");
		return selectidfrompppnetwork_url;
	}

	public static StringBuffer selectidfrompppnetwork_code = new StringBuffer();

	public static StringBuffer getSelectidfrompppnetwork_code() {
		selectidfrompppnetwork_code
				.append("select id from pp_network where network_code=?");
		return selectidfrompppnetwork_code;
	}

	public static StringBuffer select_pp_partner_option = new StringBuffer();

	public static StringBuffer getSelect_pp_partner_option() {
		select_pp_partner_option.append("SELECT * FROM pp_partner_options");
		return select_pp_partner_option;
	}

	public static StringBuffer select_frompp_partner_network = new StringBuffer();

	public static StringBuffer getSelect_frompp_partner_network() {
		select_frompp_partner_network
				.append("select * from pp_partner_network");
		return select_frompp_partner_network;
	}

	public static StringBuffer select_ppgadget_connect_size = new StringBuffer();

	public static StringBuffer getSelect_ppgadget_connect_size() {
		select_ppgadget_connect_size
				.append(" select(select partner_domain from pp_domain where id=partner_domain_id) as 'partner_domain_name',");
		select_ppgadget_connect_size
				.append(" a.css_file,a.connect_text,a.connect_style from pp_partner_options a ");

		return select_ppgadget_connect_size;
	}

public static StringBuffer select_ppgadget_myspace_app = new StringBuffer();
	
	public static StringBuffer getSelect_ppgadget_myspace_app(){
		select_ppgadget_myspace_app.append("select * from pp_gadget_myspace_application");
		return select_ppgadget_myspace_app;
	}
	
	

	public static StringBuffer insert_pp_shared_link = new StringBuffer();

	public static StringBuffer setInsert_pp_shared_link() {
		insert_pp_shared_link
				.append(" Insert into pp_shared_link (pp_id,share_social_id,network_code_id,partner_domain_id, ");
		insert_pp_shared_link
				.append(" shared_url,shared_title,shared_content,current_time_stamp) values( ");
		insert_pp_shared_link.append("?,?,?,?,?,?,?,now())");
		return insert_pp_shared_link;
	}

	public static StringBuffer select_pp_messages = new StringBuffer();

	public static StringBuffer setSelect_pp_messages() {
		select_pp_messages.append("select * from pp_messages");
		return select_pp_messages;
	}

	public static StringBuffer select_numuser_from_pp_ops_snapshot = new StringBuffer();

	public static StringBuffer setSelect_numuser_from_pp_ops_snapshot() {
		select_numuser_from_pp_ops_snapshot
				.append("SELECT num_of_users FROM pp_ops_snapshot WHERE current_time_stamp > SUBTIME(NOW(), '0 0:02:00' ) ");
		return select_numuser_from_pp_ops_snapshot;
	}

	public static StringBuffer select_numofrequest_from_pp_ops_snapshot = new StringBuffer();

	public static StringBuffer setselect_numofrequest_from_pp_ops_snapshot() {
		select_numofrequest_from_pp_ops_snapshot
				.append("SELECT num_of_request FROM pp_ops_snapshot WHERE current_time_stamp > SUBTIME(NOW(), '0 0:02:00' )");
		return select_numofrequest_from_pp_ops_snapshot;
	}

	public static StringBuffer select_totalmills_from_pp_ops_snapshot = new StringBuffer();

	public static StringBuffer setSelect_totalmills_from_pp_ops_snapshot() {
		select_totalmills_from_pp_ops_snapshot
				.append("SELECT total_milis FROM pp_ops_snapshot WHERE current_time_stamp > SUBTIME(NOW(), '0 0:02:00' )");
		return select_totalmills_from_pp_ops_snapshot;
	}

	public static StringBuffer select_payparade_server_from_pp_partner_server = new StringBuffer();

	public static StringBuffer setSelect_payparade_server_from_pp_partner_server() {
		select_payparade_server_from_pp_partner_server
				.append("SELECT payparade_server FROM pp_partner_servers where partner_host=?");
		return select_payparade_server_from_pp_partner_server;
	}

	public static StringBuffer select_pp_admin_mail = new StringBuffer();

	public static StringBuffer setSelect_pp_admin_mail() {
		select_pp_admin_mail.append(" Select * from pp_admin_mail");
		return select_pp_admin_mail;
	}

	public static StringBuffer select_from_pp_partner_servers = new StringBuffer();

	public static StringBuffer setSelect_from_pp_partner_servers() {
		select_from_pp_partner_servers
				.append("SELECT * FROM pp_partner_servers");
		return select_from_pp_partner_servers;
	}

	public static StringBuffer select_FROM_pp_ops_snapshot = new StringBuffer();

	public static StringBuffer setSelect_FROM_pp_ops_snapshot() {
		select_FROM_pp_ops_snapshot
				.append("SELECT * FROM pp_ops_snapshot WHERE id = ( select max(id) from pp_ops_snapshot )");
		return select_FROM_pp_ops_snapshot;
	}

	public static StringBuffer select_id_from_pp_network = new StringBuffer();

	public static StringBuffer setSelect_id_from_pp_network() {
		select_id_from_pp_network
				.append("SELECT id FROM payparade.pp_network where network_code=?");
		return select_id_from_pp_network;
	}

	public static StringBuffer select_distinct_social_id_friend_id_from_pp_connection = new StringBuffer();

	public static StringBuffer setSelect_distinct_social_id_friend_id_from_pp_connection() {
		select_distinct_social_id_friend_id_from_pp_connection
				.append("SELECT  friend_id, member_social_network_id FROM pp_connection WHERE network_code_id =? AND member_social_network_id =?");
		return select_distinct_social_id_friend_id_from_pp_connection;
	}

	public static StringBuffer select_logo_from_pp_partner_options = new StringBuffer();

	public static StringBuffer setSelect_logo_from_pp_partner_options() {
		select_logo_from_pp_partner_options
				.append("SELECT logo_file from pp_partner_options WHERE partner_domain_id=?");
		return select_logo_from_pp_partner_options;
	}

	public static StringBuffer select_from_pp_partner_network = new StringBuffer();

	public static StringBuffer setSelect_from_pp_partner_network() {
		select_from_pp_partner_network
				.append("SELECT a.id,(select partner_domain from pp_domain where id=a.partner_domain_id and a.partner_domain_id=?) as 'partner_domain' ,a.id_key,a.api_key,a.secret, ");
		select_from_pp_partner_network
				.append(" (select network_code from pp_network where id=a.network_code_id) as 'network_code'");
		select_from_pp_partner_network
				.append(" from pp_partner_network a where a.partner_domain_id=?");

		return select_from_pp_partner_network;
	}

	public static StringBuffer select_from_pp_partner_network_where_partner_domain_idand_network_code_id = new StringBuffer();

	public static StringBuffer setSelect_from_pp_partner_network_where_partner_domain_idand_network_code_id() {
		select_from_pp_partner_network_where_partner_domain_idand_network_code_id
				.append("SELECT a.id,(select partner_domain from pp_domain where id=a.partner_domain_id and id=?) as 'partner_domain' ,a.id_key,a.api_key,a.secret, ");
		select_from_pp_partner_network_where_partner_domain_idand_network_code_id
				.append(" (select network_code from pp_network where id=a.network_code_id and id=?) as 'network_code'");
		select_from_pp_partner_network_where_partner_domain_idand_network_code_id
				.append(" from pp_partner_network a where a.partner_domain_id=? and a.network_code_id=?");
		return select_from_pp_partner_network_where_partner_domain_idand_network_code_id;
	}

	public static StringBuffer select_from_pp_partner_options_WHERE_partner_domain = new StringBuffer();

	public static StringBuffer setSelect_from_pp_partner_options_WHERE_partner_domain() {
		select_from_pp_partner_options_WHERE_partner_domain
				.append("SELECT css_file, (select partner_domain from pp_domain where id=pp_partner_options.partner_domain_id and id=?)as 'partner_domain',");
		select_from_pp_partner_options_WHERE_partner_domain
				.append("logo_file,badge_salutation,badge_message,link_domain,connect_style,connect_text,salutation_style,message_style,");
		select_from_pp_partner_options_WHERE_partner_domain
				.append(" display_name from pp_partner_options  WHERE partner_domain_id=? ");
		return select_from_pp_partner_options_WHERE_partner_domain;
	}

	public static StringBuffer select_from_pp_partner_options_where_usermail = new StringBuffer();

	public static StringBuffer setSelect_from_pp_partner_options_where_usermail() {
		select_from_pp_partner_options_where_usermail
				.append(" SELECT pp_partner_options.display_name, (select partner_domain from pp_domain where id=pp_partner_options.partner_domain_id) as 'partner_domain',  a.ops_access, a.metrics_access, a.config_access from pp_user a, pp_partner_options  WHERE a.partner_domain_id = pp_partner_options.partner_domain_id AND user_email=? ");
		return select_from_pp_partner_options_where_usermail;
	}

	public static StringBuffer select_from_ppcustomer_rank_wherepartnerdomain_id = new StringBuffer();

	public static StringBuffer setSelect_from_ppcustomer_rank_wherepartnerdomain_id() {
		select_from_ppcustomer_rank_wherepartnerdomain_id
				.append("select measurement_dt,(select partner_domain from pp_domain where id=pp_customer_rank.partner_domain_id and id=?) as 'partner_domain', ");
		select_from_ppcustomer_rank_wherepartnerdomain_id
				.append(" (select network_code from pp_network where id=pp_customer_rank.network_code_id) as 'network_code',pp_customer_rank.network_member_social_id, ");
		select_from_ppcustomer_rank_wherepartnerdomain_id
				.append(" last_seen,brand_affinity,influence,friends,latitude,longitude");
		select_from_ppcustomer_rank_wherepartnerdomain_id
				.append(" FROM pp_customer_rank  WHERE pp_customer_rank.partner_domain_id =? LIMIT 100 ");
		return select_from_ppcustomer_rank_wherepartnerdomain_id;
	}

	public static StringBuffer select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude = new StringBuffer();

	public static StringBuffer setSelect_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude() {
		select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude
				.append(" select measurement_dt, (select partner_domain from pp_domain where id=pp_customer_rank.partner_domain_id and id=?) as 'partner_domain', ");
		select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude
				.append(" (select network_code from pp_network where id=pp_customer_rank.network_code_id) as 'network_code',network_member_social_id,");
		select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude
				.append(" last_seen,brand_affinity,influence,friends,latitude,longitude ");
		select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude
				.append(" FROM pp_customer_rank  WHERE partner_domain_id =? and ");
		select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude
				.append(" lattitude > ? and lattitude < ? AND longitude > ? AND longitude < ? LIMIT 100");

		return select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude;
	}

	public static StringBuffer select_ppsharedlink_where_shared_id = new StringBuffer();

	public static StringBuffer setSelect_ppsharedlink_where_shared_id() {
		select_ppsharedlink_where_shared_id
				.append("SELECT pp_id,share_social_id,shared_url,shared_title,shared_content, ");
		select_ppsharedlink_where_shared_id
		.append("shared_image,current_time_stamp,");
		select_ppsharedlink_where_shared_id
		.append("(select network_code from pp_network where id=pp_shared_link.network_code_id) as 'networkcode', ");
		select_ppsharedlink_where_shared_id
		.append("(select partner_domain from pp_domain where id=pp_shared_link.partner_domain_id) as 'partnerdomain' ");
		select_ppsharedlink_where_shared_id
		.append("FROM pp_shared_link WHERE id =?");
		return select_ppsharedlink_where_shared_id;
	}
	
	public static StringBuffer select_from_ppshared_link = new StringBuffer();
	
	public static StringBuffer setSelect_from_ppshared_link(){
		select_from_ppshared_link.append("select * FROM pp_shared_link WHERE id =?");
		return select_from_ppshared_link;
	}
	
	public static StringBuffer select_ppsharedlink_where_ppid = new StringBuffer();

	public static StringBuffer setSelect_ppsharedlink_where_ppid() {
		select_ppsharedlink_where_ppid
				.append(" SELECT shared_url, l.id, count(a.id) clicks, count(DISTINCT a.share_pp_id) ppid FROM pp_shared_link l LEFT OUTER JOIN pp_link_activity a ON l.id = a.id WHERE pp_id = ? GROUP by shared_url, l.id ");
		return select_ppsharedlink_where_ppid;
	}

	public static StringBuffer insert_into_ppshared_activity = new StringBuffer();

	public static StringBuffer setInsert_into_ppshared_activity() {
		insert_into_ppshared_activity
				.append(" Insert into pp_link_activity(sharee_social_id,share_pp_id) values(?,?)");
		return insert_into_ppshared_activity;
	}

	public static StringBuffer select_from_pp_connectionv1 = new StringBuffer();

	public static StringBuffer setSelect_from_pp_connectionv1() {
		select_from_pp_connectionv1
				.append("SELECT * FROM pp_connection WHERE network_code_id =? AND member_social_network_id =?");
		return select_from_pp_connectionv1;
	}

	public static StringBuffer select_from_ppuser = new StringBuffer();

	public static StringBuffer setSelect_from_ppuser() {
		select_from_ppuser
				.append("SELECT user_email FROM pp_user WHERE user_email = ? AND password_hash =?");
		return select_from_ppuser;
	}

	public static StringBuffer select_from_pp_member_network_where_networkcodeid_membersocialnetworkid = new StringBuffer();

	public static StringBuffer setSelect_from_pp_member_network_where_networkcodeid_membersocialnetworkid() {
		select_from_pp_member_network_where_networkcodeid_membersocialnetworkid
				.append("SELECT * FROM pp_member_network where network_code_id = ? AND member_social_network_id =?");
		return select_from_pp_member_network_where_networkcodeid_membersocialnetworkid;
	}

	public static StringBuffer select_from_ppmembership_where_partnerdomainid_memberid = new StringBuffer();

	public static StringBuffer setSelect_from_ppmembership_where_partnerdomainid_memberid() {
		select_from_ppmembership_where_partnerdomainid_memberid
				.append("SELECT * FROM pp_membership WHERE partner_domain_id = ? AND member_id =?");
		return select_from_ppmembership_where_partnerdomainid_memberid;
	}

	public static StringBuffer update_pp_membership = new StringBuffer();

	public static StringBuffer setUpdate_pp_membership() {
		update_pp_membership
				.append("Update pp_membership SET partners_social_key=? where partner_domain_id=? ");
		return update_pp_membership;
	}

	public static StringBuffer insert_into_pp_membership = new StringBuffer();

	public static StringBuffer setInsert_into_pp_membership() {
		insert_into_pp_membership
				.append("Insert into pp_membership (partner_domain_id,member_id,partners_social_key,connected_time_stamp)");
		insert_into_pp_membership.append(" values(?,?,?,now())");
		return insert_into_pp_membership;
	}

	public static StringBuffer insert_into_pp_membershipv1 = new StringBuffer();

	public static StringBuffer setInsert_into_pp_membershipv1() {
		insert_into_pp_membershipv1
				.append("Insert into pp_membership (partner_domain_id,member_id,partners_social_key,connected_time_stamp)");
		insert_into_pp_membershipv1
				.append(" values(?,LAST_INSERT_ID(),?,now())");
		return insert_into_pp_membershipv1;
	}

	public static StringBuffer insertinto_ppmember = new StringBuffer();

	public static StringBuffer setInsertinto_ppmember() {
		insertinto_ppmember
				.append(" Insert into pp_member (first_seen,orig_partner) ");
		insertinto_ppmember.append(" values(now(),?) ");
		return insertinto_ppmember;
	}

	public static StringBuffer insertinto_ppmembernetwork = new StringBuffer();

	public static StringBuffer setInsertinto_ppmembernetwork() {
		insertinto_ppmembernetwork
				.append(" Insert into pp_member_network(network_code_id,member_social_network_id,connected_at) ");
		insertinto_ppmembernetwork.append(" values(?,?,now()) ");
		return insertinto_ppmembernetwork;
	}

	public static StringBuffer update_pp_membershipv1 = new StringBuffer();

	public static StringBuffer setUpdate_pp_membershipv1() {
		update_pp_membershipv1
				.append("Update pp_membership SET connected_time_stamp=now() where partner_domain_id=? ");
		return update_pp_membershipv1;
	}

	public static StringBuffer insertintopp_ops_snapshot = new StringBuffer();

	public static StringBuffer setInsertintopp_ops_snapshot() {
		insertintopp_ops_snapshot
				.append("INSERT INTO pp_ops_snapshot (num_of_users, max_connections, num_of_request, total_milis, max_milis, used_memory, free_memory)");
		insertintopp_ops_snapshot.append("values(?,?,?,?,?,?,?)");
		return insertintopp_ops_snapshot;
	}

	public static StringBuffer insertIntoPPIP = new StringBuffer();

	public static StringBuffer setInsertIntoPPID() {
		insertIntoPPIP
				.append(" Insert into pp_ip(ip_address, country, region, city, postal, latitude, longitude, metro_code, area_code, isp, org)");
		insertIntoPPIP.append(" values(?,?,?,?,?,?,?,?,?,?,?)");
		return insertIntoPPIP;
	}

	public static StringBuffer insertintopp_member_session = new StringBuffer();

	public static StringBuffer setInsertintopp_member_session() {
		insertintopp_member_session
				.append(" Insert into pp_member_session(member_social_id,member_session_id,browser_id,current_time_stamp,network_code_id");
		insertintopp_member_session
				.append(" ,reference_url,pp_id,user_ip) values(?,?,?,now(),?,?,?,?)");
		return insertintopp_member_session;
	}

	public static StringBuffer select_id_from_pp_activity = new StringBuffer();

	public static StringBuffer setSelect_id_from_pp_activity() {
		select_id_from_pp_activity
				.append("select id from pp_activity where activity_code=?");
		return select_id_from_pp_activity;
	}

	public static StringBuffer select_network_name_from_networkcodeid = new StringBuffer();

	public static StringBuffer setSelect_network_name_from_networkcodeid() {
		select_network_name_from_networkcodeid
				.append("select network_code from pp_network where id=?");
		return select_network_name_from_networkcodeid;
	}

	/******************************** AuthController End *************************************************/

	public static void clearStringBuffer(StringBuffer clearStringBuffer) {
		if (clearStringBuffer != null && clearStringBuffer.length() > 0) {
			clearStringBuffer.delete(0, clearStringBuffer.length());
		}
	}

}
