--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.24
-- Dumped by pg_dump version 12.5

-- Started on 2022-07-12 05:09:31 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 16387)
-- Name: bean_cafe; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA bean_cafe;


--
-- TOC entry 206 (class 1255 OID 16388)
-- Name: reset_serial(); Type: FUNCTION; Schema: bean_cafe; Owner: -
--

CREATE FUNCTION bean_cafe.reset_serial() RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    param RECORD;
BEGIN

	FOR param IN
		-- find sequence, table and column
		SELECT TABLE_NAME, COLUMN_NAME,
			   pg_catalog.pg_get_serial_sequence(TABLE_NAME, COLUMN_NAME) AS SEQ_NAME,
			   0 AS VALUE
		  FROM INFORMATION_SCHEMA.COLUMNS
		 WHERE COLUMN_DEFAULT LIKE 'nextval(%'
		   AND TABLE_SCHEMA = 'bean_cafe'
	LOOP
		-- get max value
		EXECUTE format('SELECT MAX(%I) FROM %I',
					   param.column_name,
					   param.table_name)
		   INTO param.value;
		
		-- update current value
		PERFORM setval(param.seq_name, param.value, true);
	END LOOP;
	
END;
$$;


--
-- TOC entry 186 (class 1259 OID 16389)
-- Name: cart; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.cart (
    user_nm character varying(50) NOT NULL,
    product_id integer NOT NULL,
    option_cd character varying(20) NOT NULL,
    cnt integer DEFAULT 1 NOT NULL,
    update_dt date DEFAULT now() NOT NULL
);


--
-- TOC entry 187 (class 1259 OID 16394)
-- Name: category; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.category (
    id smallint NOT NULL,
    name character varying(20) NOT NULL,
    up_id smallint,
    ord smallint
);


--
-- TOC entry 2283 (class 0 OID 0)
-- Dependencies: 187
-- Name: TABLE category; Type: COMMENT; Schema: bean_cafe; Owner: -
--

COMMENT ON TABLE bean_cafe.category IS 'categories of products';


--
-- TOC entry 188 (class 1259 OID 16397)
-- Name: category_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: -
--

CREATE SEQUENCE bean_cafe.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2284 (class 0 OID 0)
-- Dependencies: 188
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: -
--

ALTER SEQUENCE bean_cafe.category_id_seq OWNED BY bean_cafe.category.id;


--
-- TOC entry 189 (class 1259 OID 16399)
-- Name: ord_dlvr; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.ord_dlvr (
    order_id integer NOT NULL,
    user_nm character varying(50) NOT NULL,
    sender_nm character varying(20),
    recipient_nm character varying(20) NOT NULL,
    zip_cd character varying(10) NOT NULL,
    address1 character varying(50) NOT NULL,
    address2 character varying(50),
    contact1 character varying(20) NOT NULL,
    contact2 character varying(20),
    message character varying(100)
);


--
-- TOC entry 190 (class 1259 OID 16402)
-- Name: ord_main; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.ord_main (
    id integer NOT NULL,
    user_nm character varying(50) NOT NULL,
    price integer DEFAULT 0 NOT NULL,
    delivery_price integer DEFAULT 0 NOT NULL,
    pay_id integer,
    pay_nm character varying(100),
    cash_receipt_type character varying(10),
    cash_receipt_value character varying(20),
    request_dt date,
    last_edit_dt date,
    editor_nm character varying(50),
    status_cd smallint DEFAULT 100 NOT NULL
);


--
-- TOC entry 191 (class 1259 OID 16408)
-- Name: ord_main_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: -
--

CREATE SEQUENCE bean_cafe.ord_main_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2285 (class 0 OID 0)
-- Dependencies: 191
-- Name: ord_main_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: -
--

ALTER SEQUENCE bean_cafe.ord_main_id_seq OWNED BY bean_cafe.ord_main.id;


--
-- TOC entry 192 (class 1259 OID 16410)
-- Name: ord_prod; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.ord_prod (
    order_id integer NOT NULL,
    product_id integer NOT NULL,
    option_cd character varying(20) NOT NULL,
    seller_nm character varying(50) NOT NULL,
    price integer DEFAULT 0 NOT NULL,
    discount_price integer DEFAULT 0 NOT NULL,
    cnt integer DEFAULT 1 NOT NULL,
    product_nm character varying(50),
    option_nm character varying(50),
    status_cd smallint,
    delivery_id integer
);


--
-- TOC entry 193 (class 1259 OID 16416)
-- Name: ord_prod_dlvr; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.ord_prod_dlvr (
    order_id integer NOT NULL,
    delivery_id integer NOT NULL,
    seller_nm character varying(20),
    type_nm character varying(20) NOT NULL,
    delivery_nm character varying(20),
    price integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 194 (class 1259 OID 16420)
-- Name: pay_type; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.pay_type (
    id integer NOT NULL,
    type character varying(15) NOT NULL,
    option1 character varying(50),
    option2 character varying(50),
    enabled boolean DEFAULT true NOT NULL
);


--
-- TOC entry 195 (class 1259 OID 16424)
-- Name: pay_type_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: -
--

CREATE SEQUENCE bean_cafe.pay_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2286 (class 0 OID 0)
-- Dependencies: 195
-- Name: pay_type_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: -
--

ALTER SEQUENCE bean_cafe.pay_type_id_seq OWNED BY bean_cafe.pay_type.id;


--
-- TOC entry 196 (class 1259 OID 16426)
-- Name: prod_det; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.prod_det (
    product_id integer NOT NULL,
    option_cd character varying(20) NOT NULL,
    full_nm character varying(100) NOT NULL,
    price_change integer DEFAULT 0 NOT NULL,
    stock_cnt integer DEFAULT 0,
    enabled boolean DEFAULT false NOT NULL
);


--
-- TOC entry 197 (class 1259 OID 16432)
-- Name: prod_dlvr; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.prod_dlvr (
    id integer NOT NULL,
    seller_nm character varying(50) NOT NULL,
    type character varying(10) DEFAULT 'ETC'::character varying NOT NULL,
    name character varying(20),
    price integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 198 (class 1259 OID 16437)
-- Name: prod_dlvr_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: -
--

CREATE SEQUENCE bean_cafe.prod_dlvr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2287 (class 0 OID 0)
-- Dependencies: 198
-- Name: prod_dlvr_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: -
--

ALTER SEQUENCE bean_cafe.prod_dlvr_id_seq OWNED BY bean_cafe.prod_dlvr.id;


--
-- TOC entry 199 (class 1259 OID 16439)
-- Name: prod_main; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.prod_main (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    price integer NOT NULL,
    category_id smallint,
    enabled boolean DEFAULT false NOT NULL,
    seller_nm character varying(50),
    stock_cnt integer,
    delivery_id integer,
    delivery_price integer,
    discount_price integer
);


--
-- TOC entry 200 (class 1259 OID 16443)
-- Name: prod_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: -
--

CREATE SEQUENCE bean_cafe.prod_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2288 (class 0 OID 0)
-- Dependencies: 200
-- Name: prod_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: -
--

ALTER SEQUENCE bean_cafe.prod_id_seq OWNED BY bean_cafe.prod_main.id;


--
-- TOC entry 201 (class 1259 OID 16445)
-- Name: prod_opt; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.prod_opt (
    product_id integer NOT NULL,
    option_group smallint NOT NULL,
    option_id character(2) DEFAULT '00'::bpchar NOT NULL,
    name character varying(20) NOT NULL,
    ord smallint DEFAULT 0 NOT NULL,
    CONSTRAINT option_id_length CHECK ((length(option_id) = 2))
);


--
-- TOC entry 2289 (class 0 OID 0)
-- Dependencies: 201
-- Name: TABLE prod_opt; Type: COMMENT; Schema: bean_cafe; Owner: -
--

COMMENT ON TABLE bean_cafe.prod_opt IS 'base data of option_cd';


--
-- TOC entry 202 (class 1259 OID 16451)
-- Name: prod_tag; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.prod_tag (
    product_id integer NOT NULL,
    name character varying(15) NOT NULL
);


--
-- TOC entry 203 (class 1259 OID 16454)
-- Name: ship_main; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.ship_main (
    order_id integer NOT NULL,
    seller_nm character varying(50) NOT NULL,
    price integer NOT NULL,
    delivery_price integer DEFAULT 0 NOT NULL,
    status_cd character varying(3)
);


--
-- TOC entry 204 (class 1259 OID 16458)
-- Name: user_auth; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.user_auth (
    user_nm character varying(50) NOT NULL,
    authority character varying(50) NOT NULL
);


--
-- TOC entry 205 (class 1259 OID 16461)
-- Name: user_main; Type: TABLE; Schema: bean_cafe; Owner: -
--

CREATE TABLE bean_cafe.user_main (
    user_nm character varying(50) NOT NULL,
    pwd character varying(50) NOT NULL,
    enabled boolean DEFAULT true NOT NULL
);


--
-- TOC entry 2070 (class 2604 OID 16465)
-- Name: category id; Type: DEFAULT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.category ALTER COLUMN id SET DEFAULT nextval('bean_cafe.category_id_seq'::regclass);


--
-- TOC entry 2074 (class 2604 OID 16466)
-- Name: ord_main id; Type: DEFAULT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_main ALTER COLUMN id SET DEFAULT nextval('bean_cafe.ord_main_id_seq'::regclass);


--
-- TOC entry 2080 (class 2604 OID 16467)
-- Name: pay_type id; Type: DEFAULT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.pay_type ALTER COLUMN id SET DEFAULT nextval('bean_cafe.pay_type_id_seq'::regclass);


--
-- TOC entry 2087 (class 2604 OID 16468)
-- Name: prod_dlvr id; Type: DEFAULT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_dlvr ALTER COLUMN id SET DEFAULT nextval('bean_cafe.prod_dlvr_id_seq'::regclass);


--
-- TOC entry 2089 (class 2604 OID 16469)
-- Name: prod_main id; Type: DEFAULT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_main ALTER COLUMN id SET DEFAULT nextval('bean_cafe.prod_id_seq'::regclass);


--
-- TOC entry 2258 (class 0 OID 16389)
-- Dependencies: 186
-- Data for Name: cart; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.cart (user_nm, product_id, option_cd, cnt, update_dt) FROM stdin;
admin	2	010101	1	2020-10-13
customer1	3	0201	1	2020-10-18
customer1	2	010201	1	2020-10-18
\.


--
-- TOC entry 2259 (class 0 OID 16394)
-- Dependencies: 187
-- Data for Name: category; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.category (id, name, up_id, ord) FROM stdin;
6	filter	5	1
5	accessory	0	2
7	bean	0	1
2	blending	7	1
3	africa	7	2
4	south america	7	3
1	etc	0	3
\.


--
-- TOC entry 2261 (class 0 OID 16399)
-- Dependencies: 189
-- Data for Name: ord_dlvr; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.ord_dlvr (order_id, user_nm, sender_nm, recipient_nm, zip_cd, address1, address2, contact1, contact2, message) FROM stdin;
1	customer1	발송자	수신자	32800	충청남도 계룡시 계룡대로 663	사서함 501-329	010-0000-1111	042-552-3668	파손주의. 조심히 배송 부탁드립니다
\.


--
-- TOC entry 2262 (class 0 OID 16402)
-- Dependencies: 190
-- Data for Name: ord_main; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.ord_main (id, user_nm, price, delivery_price, pay_id, pay_nm, cash_receipt_type, cash_receipt_value, request_dt, last_edit_dt, editor_nm, status_cd) FROM stdin;
1	customer1	10200	5500	1	\N	\N	\N	2020-10-18	2020-10-18	customer1	100
\.


--
-- TOC entry 2264 (class 0 OID 16410)
-- Dependencies: 192
-- Data for Name: ord_prod; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.ord_prod (order_id, product_id, option_cd, seller_nm, price, discount_price, cnt, product_nm, option_nm, status_cd, delivery_id) FROM stdin;
1	3	0201	customer1	8500	-800	1	\N	\N	100	2
1	2	010201	customer1	3000	0	1	\N	\N	100	1
\.


--
-- TOC entry 2265 (class 0 OID 16416)
-- Dependencies: 193
-- Data for Name: ord_prod_dlvr; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.ord_prod_dlvr (order_id, delivery_id, seller_nm, type_nm, delivery_nm, price) FROM stdin;
\.


--
-- TOC entry 2266 (class 0 OID 16420)
-- Dependencies: 194
-- Data for Name: pay_type; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.pay_type (id, type, option1, option2, enabled) FROM stdin;
1	TRANSFER	국민은행	\N	t
2	TRANSFER	하나은행	\N	t
3	CARD	우리카드	무이자	t
4	CARD	우리카드	12개월	t
5	PHONE	SKT	\N	t
6	API	네이버페이	\N	t
7	API	토스	\N	t
8	API	카카오페이	\N	t
\.


--
-- TOC entry 2268 (class 0 OID 16426)
-- Dependencies: 196
-- Data for Name: prod_det; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.prod_det (product_id, option_cd, full_nm, price_change, stock_cnt, enabled) FROM stdin;
2	010101	grind level : Whole Bean / roast level : Low / volume : 100g	-4000	10	t
2	010201	grind level : Whole Bean / roast level : High / volume : 100g	-4000	12	t
2	020202	grind level : French Press / roast level : High / volume : 220g	0	12	t
3	0101	Volume : 250g / Package : no package	0	5	t
3	0102	Volume : 500g / Package : no package	5000	2	t
3	0201	Volume : 250g / Package : for gift	500	3	t
3	0202	Volume : 500g / Package : for gift	5500	4	t
\.


--
-- TOC entry 2269 (class 0 OID 16432)
-- Dependencies: 197
-- Data for Name: prod_dlvr; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.prod_dlvr (id, seller_nm, type, name, price) FROM stdin;
1	admin	DOMESTIC	\N	2500
2	seller1	DOMESTIC	\N	3000
\.


--
-- TOC entry 2271 (class 0 OID 16439)
-- Dependencies: 199
-- Data for Name: prod_main; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.prod_main (id, name, price, category_id, enabled, seller_nm, stock_cnt, delivery_id, delivery_price, discount_price) FROM stdin;
2	Bean Cafe Main Blending	7000	2	t	admin	\N	1	2500	0
3	Africa Special Beans	8000	3	t	seller1	\N	2	3000	-800
\.


--
-- TOC entry 2273 (class 0 OID 16445)
-- Dependencies: 201
-- Data for Name: prod_opt; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.prod_opt (product_id, option_group, option_id, name, ord) FROM stdin;
2	1	01	Whole Bean	1
2	1	02	French Press	2
2	1	03	Drip&Coffee Maker	3
2	1	04	Dutch&Moka Pot	4
2	1	05	Espresso	5
2	2	00	roast level	0
2	2	01	Low	1
2	2	02	High	2
2	3	00	Volume	0
2	3	01	100g	1
2	3	02	220g	2
2	3	03	500g	3
2	1	00	grind level	0
3	1	00	Volume	0
3	1	01	250g	1
3	1	02	500g	2
3	2	00	Package	0
3	2	01	no package	1
3	2	02	for gift	2
\.


--
-- TOC entry 2274 (class 0 OID 16451)
-- Dependencies: 202
-- Data for Name: prod_tag; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.prod_tag (product_id, name) FROM stdin;
\.


--
-- TOC entry 2275 (class 0 OID 16454)
-- Dependencies: 203
-- Data for Name: ship_main; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.ship_main (order_id, seller_nm, price, delivery_price, status_cd) FROM stdin;
1	admin	3000	2500	100
1	seller1	7700	3000	100
\.


--
-- TOC entry 2276 (class 0 OID 16458)
-- Dependencies: 204
-- Data for Name: user_auth; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.user_auth (user_nm, authority) FROM stdin;
seller1	seller
admin	admin
\.


--
-- TOC entry 2277 (class 0 OID 16461)
-- Dependencies: 205
-- Data for Name: user_main; Type: TABLE DATA; Schema: bean_cafe; Owner: -
--

COPY bean_cafe.user_main (user_nm, pwd, enabled) FROM stdin;
admin	root	t
customer1	customer1	t
customer2	customer2	t
seller1	seller1	t
\.


--
-- TOC entry 2290 (class 0 OID 0)
-- Dependencies: 188
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: bean_cafe; Owner: -
--

SELECT pg_catalog.setval('bean_cafe.category_id_seq', 7, true);


--
-- TOC entry 2291 (class 0 OID 0)
-- Dependencies: 191
-- Name: ord_main_id_seq; Type: SEQUENCE SET; Schema: bean_cafe; Owner: -
--

SELECT pg_catalog.setval('bean_cafe.ord_main_id_seq', 1, true);


--
-- TOC entry 2292 (class 0 OID 0)
-- Dependencies: 195
-- Name: pay_type_id_seq; Type: SEQUENCE SET; Schema: bean_cafe; Owner: -
--

SELECT pg_catalog.setval('bean_cafe.pay_type_id_seq', 8, true);


--
-- TOC entry 2293 (class 0 OID 0)
-- Dependencies: 198
-- Name: prod_dlvr_id_seq; Type: SEQUENCE SET; Schema: bean_cafe; Owner: -
--

SELECT pg_catalog.setval('bean_cafe.prod_dlvr_id_seq', 2, true);


--
-- TOC entry 2294 (class 0 OID 0)
-- Dependencies: 200
-- Name: prod_id_seq; Type: SEQUENCE SET; Schema: bean_cafe; Owner: -
--

SELECT pg_catalog.setval('bean_cafe.prod_id_seq', 3, true);


--
-- TOC entry 2096 (class 2606 OID 16471)
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (user_nm, product_id, option_cd);


--
-- TOC entry 2099 (class 2606 OID 16473)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 2115 (class 2606 OID 16475)
-- Name: prod_dlvr delivery_group_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_dlvr
    ADD CONSTRAINT delivery_group_pkey PRIMARY KEY (id);


--
-- TOC entry 2124 (class 2606 OID 16477)
-- Name: ship_main delivery_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ship_main
    ADD CONSTRAINT delivery_pkey PRIMARY KEY (order_id, seller_nm);


--
-- TOC entry 2084 (class 2606 OID 16478)
-- Name: prod_det option_cd_length; Type: CHECK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE bean_cafe.prod_det
    ADD CONSTRAINT option_cd_length CHECK (((length((option_cd)::text) % 2) = 0)) NOT VALID;


--
-- TOC entry 2113 (class 2606 OID 16480)
-- Name: prod_det option_detail_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_det
    ADD CONSTRAINT option_detail_pkey PRIMARY KEY (product_id, option_cd);


--
-- TOC entry 2109 (class 2606 OID 16482)
-- Name: ord_prod_dlvr ord_prod_dlvr_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_prod_dlvr
    ADD CONSTRAINT ord_prod_dlvr_pkey PRIMARY KEY (order_id, delivery_id);


--
-- TOC entry 2107 (class 2606 OID 16484)
-- Name: ord_prod ord_prod_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_prod
    ADD CONSTRAINT ord_prod_pkey PRIMARY KEY (order_id, product_id, option_cd);


--
-- TOC entry 2101 (class 2606 OID 16486)
-- Name: ord_dlvr order_delivery_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_dlvr
    ADD CONSTRAINT order_delivery_pkey PRIMARY KEY (order_id);


--
-- TOC entry 2103 (class 2606 OID 16488)
-- Name: ord_main order_main_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_main
    ADD CONSTRAINT order_main_pkey PRIMARY KEY (id);


--
-- TOC entry 2105 (class 2606 OID 16490)
-- Name: ord_main order_main_ukey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_main
    ADD CONSTRAINT order_main_ukey UNIQUE (user_nm, id);


--
-- TOC entry 2111 (class 2606 OID 16492)
-- Name: pay_type pay_type_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.pay_type
    ADD CONSTRAINT pay_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2120 (class 2606 OID 16494)
-- Name: prod_opt product_option_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_opt
    ADD CONSTRAINT product_option_pkey PRIMARY KEY (product_id, option_group, option_id);


--
-- TOC entry 2117 (class 2606 OID 16496)
-- Name: prod_main product_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_main
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 2122 (class 2606 OID 16498)
-- Name: prod_tag product_tag_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_tag
    ADD CONSTRAINT product_tag_pkey PRIMARY KEY (product_id, name);


--
-- TOC entry 2126 (class 2606 OID 16500)
-- Name: user_auth user_auth_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.user_auth
    ADD CONSTRAINT user_auth_pkey PRIMARY KEY (user_nm, authority);


--
-- TOC entry 2128 (class 2606 OID 16502)
-- Name: user_main user_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.user_main
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_nm);


--
-- TOC entry 2097 (class 1259 OID 16503)
-- Name: category_idx_uk; Type: INDEX; Schema: bean_cafe; Owner: -
--

CREATE UNIQUE INDEX category_idx_uk ON bean_cafe.category USING btree (up_id, ord);


--
-- TOC entry 2118 (class 1259 OID 16504)
-- Name: product_option_idx_uk; Type: INDEX; Schema: bean_cafe; Owner: -
--

CREATE UNIQUE INDEX product_option_idx_uk ON bean_cafe.prod_opt USING btree (product_id, option_group, ord);


--
-- TOC entry 2140 (class 2606 OID 16505)
-- Name: user_auth fk_authorities_users; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.user_auth
    ADD CONSTRAINT fk_authorities_users FOREIGN KEY (user_nm) REFERENCES bean_cafe.user_main(user_nm) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2129 (class 2606 OID 16510)
-- Name: cart fk_cart_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.cart
    ADD CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES bean_cafe.prod_main(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2130 (class 2606 OID 16515)
-- Name: cart fk_cart_user; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.cart
    ADD CONSTRAINT fk_cart_user FOREIGN KEY (user_nm) REFERENCES bean_cafe.user_main(user_nm) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2136 (class 2606 OID 16520)
-- Name: prod_det fk_option_detail_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_det
    ADD CONSTRAINT fk_option_detail_product FOREIGN KEY (product_id) REFERENCES bean_cafe.prod_main(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 2139 (class 2606 OID 16525)
-- Name: prod_opt fk_option_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_opt
    ADD CONSTRAINT fk_option_product FOREIGN KEY (product_id) REFERENCES bean_cafe.prod_main(id) ON UPDATE CASCADE;


--
-- TOC entry 2134 (class 2606 OID 16530)
-- Name: ord_prod_dlvr fk_ord_prod_dlvr_main; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_prod_dlvr
    ADD CONSTRAINT fk_ord_prod_dlvr_main FOREIGN KEY (order_id) REFERENCES bean_cafe.ord_main(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2135 (class 2606 OID 16535)
-- Name: ord_prod_dlvr fk_ord_prod_dlvr_prod_dlvr; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_prod_dlvr
    ADD CONSTRAINT fk_ord_prod_dlvr_prod_dlvr FOREIGN KEY (delivery_id) REFERENCES bean_cafe.prod_dlvr(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 2131 (class 2606 OID 16540)
-- Name: ord_dlvr fk_order_deliver_main; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_dlvr
    ADD CONSTRAINT fk_order_deliver_main FOREIGN KEY (order_id, user_nm) REFERENCES bean_cafe.ord_main(id, user_nm) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2133 (class 2606 OID 16545)
-- Name: ord_prod fk_order_product_main; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_prod
    ADD CONSTRAINT fk_order_product_main FOREIGN KEY (order_id) REFERENCES bean_cafe.ord_main(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2132 (class 2606 OID 16550)
-- Name: ord_main fk_order_user; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.ord_main
    ADD CONSTRAINT fk_order_user FOREIGN KEY (user_nm) REFERENCES bean_cafe.user_main(user_nm) ON UPDATE CASCADE;


--
-- TOC entry 2137 (class 2606 OID 16555)
-- Name: prod_main fk_product_category; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_main
    ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES bean_cafe.category(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 2138 (class 2606 OID 16560)
-- Name: prod_main fk_product_users; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: -
--

ALTER TABLE ONLY bean_cafe.prod_main
    ADD CONSTRAINT fk_product_users FOREIGN KEY (seller_nm) REFERENCES bean_cafe.user_main(user_nm) ON UPDATE CASCADE ON DELETE SET NULL;


-- Completed on 2022-07-12 05:09:31 UTC

--
-- PostgreSQL database dump complete
--

