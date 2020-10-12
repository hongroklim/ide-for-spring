--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.19
-- Dumped by pg_dump version 12.4

-- Started on 2020-10-12 11:48:16 UTC

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
-- TOC entry 2180 (class 0 OID 0)
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
-- TOC entry 2181 (class 0 OID 0)
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
-- TOC entry 189 (class 1259 OID 16430)
-- Name: product; Type: TABLE; Schema: bean_cafe; Owner: bean_cafe_dev
--

CREATE TABLE bean_cafe.product (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    price smallint NOT NULL,
    category_id smallint,
    enabled boolean DEFAULT false NOT NULL,
    seller_nm character varying(50),
    stock_cnt integer,
    deliver_price smallint DEFAULT 0 NOT NULL,
    discount_price smallint DEFAULT 0 NOT NULL
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
-- TOC entry 2182 (class 0 OID 0)
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
-- TOC entry 2183 (class 0 OID 0)
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
-- TOC entry 2029 (class 2604 OID 16422)
-- Name: category id; Type: DEFAULT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.category ALTER COLUMN id SET DEFAULT nextval('bean_cafe.category_id_seq'::regclass);


--
-- TOC entry 2030 (class 2604 OID 16433)
-- Name: product id; Type: DEFAULT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product ALTER COLUMN id SET DEFAULT nextval('bean_cafe.product_id_seq'::regclass);


--
-- TOC entry 2042 (class 2606 OID 16427)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 2040 (class 2606 OID 24623)
-- Name: option_detail option_cd_length; Type: CHECK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE bean_cafe.option_detail
    ADD CONSTRAINT option_cd_length CHECK (((length((option_cd)::text) % 2) = 0)) NOT VALID;


--
-- TOC entry 2050 (class 2606 OID 24601)
-- Name: option_detail option_detail_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.option_detail
    ADD CONSTRAINT option_detail_pkey PRIMARY KEY (product_id, option_cd);


--
-- TOC entry 2036 (class 2606 OID 24621)
-- Name: product_option option_id_length; Type: CHECK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE bean_cafe.product_option
    ADD CONSTRAINT option_id_length CHECK ((length(option_id) = 2)) NOT VALID;


--
-- TOC entry 2048 (class 2606 OID 24596)
-- Name: product_option product_option_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product_option
    ADD CONSTRAINT product_option_pkey PRIMARY KEY (product_id, option_group, option_id);


--
-- TOC entry 2044 (class 2606 OID 16435)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 2052 (class 2606 OID 24628)
-- Name: product_tag product_tag_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product_tag
    ADD CONSTRAINT product_tag_pkey PRIMARY KEY (product_id, name);


--
-- TOC entry 2046 (class 2606 OID 16442)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_nm);


--
-- TOC entry 2055 (class 2606 OID 16446)
-- Name: user_auth fk_authorities_users; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.user_auth
    ADD CONSTRAINT fk_authorities_users FOREIGN KEY (user_nm) REFERENCES bean_cafe."user"(user_nm) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2057 (class 2606 OID 24612)
-- Name: option_detail fk_option_detail_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.option_detail
    ADD CONSTRAINT fk_option_detail_product FOREIGN KEY (product_id) REFERENCES bean_cafe.product(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 2056 (class 2606 OID 24607)
-- Name: product_option fk_option_product; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product_option
    ADD CONSTRAINT fk_option_product FOREIGN KEY (product_id) REFERENCES bean_cafe.product(id) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 2053 (class 2606 OID 16451)
-- Name: product fk_product_category; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product
    ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES bean_cafe.category(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 2054 (class 2606 OID 16466)
-- Name: product fk_product_users; Type: FK CONSTRAINT; Schema: bean_cafe; Owner: bean_cafe_dev
--

ALTER TABLE ONLY bean_cafe.product
    ADD CONSTRAINT fk_product_users FOREIGN KEY (seller_nm) REFERENCES bean_cafe."user"(user_nm) ON UPDATE CASCADE ON DELETE SET NULL;


-- Completed on 2020-10-12 11:48:16 UTC

--
-- PostgreSQL database dump complete
--

