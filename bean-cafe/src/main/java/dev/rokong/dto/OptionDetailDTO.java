package dev.rokong.dto;

public class OptionDetailDTO {
    private int productId;
    private String optionCd;
    private String fullNm;
    private int priceChange;
    private int stockCnt;
    private boolean enabled;

    public int getProductId() {
        return productId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getStockCnt() {
        return stockCnt;
    }

    public void setStockCnt(int stockCnt) {
        this.stockCnt = stockCnt;
    }

    public int getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(int priceChange) {
        this.priceChange = priceChange;
    }

    public String getFullNm() {
		return fullNm;
	}

	public void setFullNm(String fullNm) {
		this.fullNm = fullNm;
	}

	public String getOptionCd() {
        return optionCd;
    }

    public void setOptionCd(String optionCd) {
        this.optionCd = optionCd;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
}