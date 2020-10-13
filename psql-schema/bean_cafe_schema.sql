--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.19
-- Dumped by pg_dump version 12.4

-- Started on 2020-10-13 11:53:34 UTC

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
-- TOC entry 5 (class 2615 OID 16403)
-- Name: bean_cafe; Type: SCHEMA; Schema: -; Owner: bean_cafe_dev
--

CREATE SCHEMA bean_cafe;


ALTER SCHEMA bean_cafe OWNER TO bean_cafe_dev;

SET default_tablespace = '';

--
-- TOC entry 195 (class 1259 OID 24632)
-- Name: cart; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.cart (
    user_nm character varying(50) NOT NULL,
    product_id integer NOT NULL,
    option_cd character varying(20) NOT NULL,
    cnt integer,
    update_dt date DEFAULT now() NOT NULL
);


ALTER TABLE bean_cafe.cart OWNER TO bean_cafe_dev;

--
-- TOC entry 186 (class 1259 OID 16409)
-- Name: category; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.category (
    id smallint NOT NULL,
    name character varying(20) NOT NULL,
    up_id smallint,
    "order" smallint
);


ALTER TABLE bean_cafe.category OWNER TO bean_cafe_dev;

--
-- TOC entry 2216 (class 0 OID 0)
-- Dependencies: 186
-- Name: TABLE category; Type: COMMENT; Schema: bean_cafe; Owner: bean_cafe_dev
--

COMMENT ON TABLE bean_cafe.category IS 'categories of products';


--
-- TOC entry 187 (class 1259 OID 16420)
-- Name: category_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE SEQUENCE bean_cafe.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bean_cafe.category_id_seq OWNER TO bean_cafe_dev;

--
-- TOC entry 2217 (class 0 OID 0)
-- Dependencies: 187
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER SEQUENCE bean_cafe.category_id_seq OWNED BY bean_cafe.category.id;


--
-- TOC entry 193 (class 1259 OID 24597)
-- Name: option_detail; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.option_detail (
    product_id integer NOT NULL,
    option_cd character varying(20) NOT NULL,
    full_nm character varying(100) NOT NULL,
    price_change integer DEFAULT 0 NOT NULL,
    stock_cnt integer DEFAULT 0,
    enabled boolean DEFAULT false NOT NULL
);


ALTER TABLE bean_cafe.option_detail OWNER TO bean_cafe_dev;

--
-- TOC entry 198 (class 1259 OID 24683)
-- Name: order_deliver; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.order_deliver (
    order_id integer NOT NULL,
    user_nm character varying(50) NOT NULL,
    sender_nm character varying(20),
    recipient_nm character varying(20) NOT NULL,
    zip_cd character varying(10) NOT NULL,
    address1 character varying(50) NOT NULL,
    address2 character varying(50),
    contact character varying(20) NOT NULL,
    method character varying(20) NOT NULL,
    method_detail character varying(20),
    message character varying(50)
);


ALTER TABLE bean_cafe.order_deliver OWNER TO bean_cafe_dev;

--
-- TOC entry 197 (class 1259 OID 24669)
-- Name: order_main; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.order_main (
    id integer NOT NULL,
    user_nm character varying(50) NOT NULL,
    total_price integer DEFAULT 0 NOT NULL,
    discount_price integer DEFAULT 0 NOT NULL,
    deliver_price integer DEFAULT 0 NOT NULL,
    pay_type character varying(10),
    pay_detail character varying(20),
    cash_receipt_type character varying(10),
    cash_receipt_value character varying(20),
    request_dt date,
    receive_dt date
);


ALTER TABLE bean_cafe.order_main OWNER TO bean_cafe_dev;

--
-- TOC entry 196 (class 1259 OID 24667)
-- Name: order_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE SEQUENCE bean_cafe.order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bean_cafe.order_id_seq OWNER TO bean_cafe_dev;

--
-- TOC entry 2218 (class 0 OID 0)
-- Dependencies: 196
-- Name: order_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER SEQUENCE bean_cafe.order_id_seq OWNED BY bean_cafe.order_main.id;


--
-- TOC entry 199 (class 1259 OID 24701)
-- Name: order_product; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.order_product (
    order_id integer NOT NULL,
    product_id integer NOT NULL,
    option_cd character varying(20) NOT NULL
);


ALTER TABLE bean_cafe.order_product OWNER TO bean_cafe_dev;

--
-- TOC entry 189 (class 1259 OID 16430)
-- Name: product; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.product (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    price integer NOT NULL,
    category_id smallint,
    enabled boolean DEFAULT false NOT NULL,
    seller_nm character varying(50),
    stock_cnt integer,
    deliver_price integer DEFAULT 0 NOT NULL,
    discount_price integer DEFAULT 0 NOT NULL
);


ALTER TABLE bean_cafe.product OWNER TO bean_cafe_dev;

--
-- TOC entry 188 (class 1259 OID 16428)
-- Name: product_id_seq; Type: SEQUENCE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE SEQUENCE bean_cafe.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bean_cafe.product_id_seq OWNER TO bean_cafe_dev;

--
-- TOC entry 2219 (class 0 OID 0)
-- Dependencies: 188
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER SEQUENCE bean_cafe.product_id_seq OWNED BY bean_cafe.product.id;


--
-- TOC entry 192 (class 1259 OID 24592)
-- Name: product_option; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.product_option (
    product_id integer NOT NULL,
    option_group smallint NOT NULL,
    option_id character(2) DEFAULT '00'::bpchar NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE bean_cafe.product_option OWNER TO bean_cafe_dev;

--
-- TOC entry 2220 (class 0 OID 0)
-- Dependencies: 192
-- Name: TABLE product_option; Type: COMMENT; Schema: bean_cafe; Owner: bean_cafe_dev
--

COMMENT ON TABLE bean_cafe.product_option IS 'base data of option_cd';


--
-- TOC entry 194 (class 1259 OID 24624)
-- Name: product_tag; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.product_tag (
    product_id integer NOT NULL,
    name character varying(15) NOT NULL
);


ALTER TABLE bean_cafe.product_tag OWNER TO bean_cafe_dev;

--
-- TOC entry 190 (class 1259 OID 16438)
-- Name: user; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe."user" (
    user_nm character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    enabled boolean DEFAULT true NOT NULL
);


ALTER TABLE bean_cafe."user" OWNER TO bean_cafe_dev;

--
-- TOC entry 191 (class 1259 OID 16443)
-- Name: user_auth; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.user_auth (
    user_nm character varying(50) NOT NULL,
    authority character varying(50) NOT NULL
);


ALTER TABLE bean_cafe.user_auth OWNER TO bean_cafe_dev;

--
-- TOC entry 2047 (class 2604 OID 16422)
-- Name: category id; Type: DEFAULT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.category ALTER COLUMN id SET DEFAULT nextval('bean_cafe.category_id_seq'::regclass);


--
-- TOC entry 2060 (class 2604 OID 24672)
-- Name: order_main id; Type: DEFAULT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_main ALTER COLUMN id SET DEFAULT nextval('bean_cafe.order_id_seq'::regclass);


--
-- TOC entry 2048 (class 2604 OID 16433)
-- Name: product id; Type: DEFAULT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product ALTER COLUMN id SET DEFAULT nextval('bean_cafe.product_id_seq'::regclass);


--
-- TOC entry 2077 (class 2606 OID 24636)
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (user_nm, product_id, option_cd);


--
-- TOC entry 2065 (class 2606 OID 16427)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 2058 (class 2606 OID 24623)
-- Name: option_detail option_cd_length; Type: CHECK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE bean_cafe.option_detail
    ADD CONSTRAINT option_cd_length CHECK (((length((option_cd)::text) % 2) = 0)) NOT VALID;


--
-- TOC entry 2073 (class 2606 OID 24601)
-- Name: option_detail option_detail_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.option_detail
    ADD CONSTRAINT option_detail_pkey PRIMARY KEY (product_id, option_cd);


--
-- TOC entry 2054 (class 2606 OID 24621)
-- Name: product_option option_id_length; Type: CHECK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE bean_cafe.product_option
    ADD CONSTRAINT option_id_length CHECK ((length(option_id) = 2)) NOT VALID;


--
-- TOC entry 2081 (class 2606 OID 24690)
-- Name: order_deliver order_deliver_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_deliver
    ADD CONSTRAINT order_deliver_pkey PRIMARY KEY (order_id);


--
-- TOC entry 2079 (class 2606 OID 24674)
-- Name: order_main order_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_main
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);


--
-- TOC entry 2083 (class 2606 OID 24705)
-- Name: order_product order_product_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_product
    ADD CONSTRAINT order_product_pkey PRIMARY KEY (order_id, product_id, option_cd);


--
-- TOC entry 2071 (class 2606 OID 24596)
-- Name: product_option product_option_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product_option
    ADD CONSTRAINT product_option_pkey PRIMARY KEY (product_id, option_group, option_id);


--
-- TOC entry 2067 (class 2606 OID 16435)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 2075 (class 2606 OID 24628)
-- Name: product_tag product_tag_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product_tag
    ADD CONSTRAINT product_tag_pkey PRIMARY KEY (product_id, name);


--
-- TOC entry 2069 (class 2606 OID 16442)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_nm);


--
-- TOC entry 2086 (class 2606 OID 16446)
-- Name: user_auth fk_authorities_users; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.user_auth
    ADD CONSTRAINT fk_authorities_users FOREIGN KEY (user_nm) REFERENCES bean_cafe."user"(user_nm) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2090 (class 2606 OID 24647)
-- Name: cart fk_cart_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.cart
    ADD CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES bean_cafe.product(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 2089 (class 2606 OID 24642)
-- Name: cart fk_cart_user; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.cart
    ADD CONSTRAINT fk_cart_user FOREIGN KEY (user_nm) REFERENCES bean_cafe."user"(user_nm) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2088 (class 2606 OID 24612)
-- Name: option_detail fk_option_detail_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.option_detail
    ADD CONSTRAINT fk_option_detail_product FOREIGN KEY (product_id) REFERENCES bean_cafe.product(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 2087 (class 2606 OID 24607)
-- Name: product_option fk_option_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product_option
    ADD CONSTRAINT fk_option_product FOREIGN KEY (product_id) REFERENCES bean_cafe.product(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 2092 (class 2606 OID 24691)
-- Name: order_deliver fk_order_deliver_main; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_deliver
    ADD CONSTRAINT fk_order_deliver_main FOREIGN KEY (order_id) REFERENCES bean_cafe.order_main(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2093 (class 2606 OID 24706)
-- Name: order_product fk_order_product_main; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_product
    ADD CONSTRAINT fk_order_product_main FOREIGN KEY (order_id) REFERENCES bean_cafe.order_main(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2091 (class 2606 OID 24675)
-- Name: order_main fk_order_user; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.order_main
    ADD CONSTRAINT fk_order_user FOREIGN KEY (user_nm) REFERENCES bean_cafe."user"(user_nm) ON UPDATE CASCADE;


--
-- TOC entry 2084 (class 2606 OID 16451)
-- Name: product fk_product_category; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product
    ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES bean_cafe.category(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 2085 (class 2606 OID 16466)
-- Name: product fk_product_users; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product
    ADD CONSTRAINT fk_product_users FOREIGN KEY (seller_nm) REFERENCES bean_cafe."user"(user_nm) ON UPDATE CASCADE ON DELETE SET NULL;


-- Completed on 2020-10-13 11:53:35 UTC

--
-- PostgreSQL database dump complete
--

