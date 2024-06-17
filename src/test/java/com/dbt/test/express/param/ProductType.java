package com.dbt.test.express.param;

public enum ProductType {
    FAST_FOOD(1, "快餐"),
    MEDICINE(2, "药品"),
    DEPARTMENT_STORE(3, "百货"),
    DIRTY_CLOTHES_COLLECT(4, "脏衣服收"),
    CLEAN_CLOTHES_DISPATCH(5, "干净衣服派"),
    FRESH(6, "生鲜"),
    HIGH_END_DRINKS(8, "高端饮品"),
    ON_SITE_SURVEY(9, "现场勘验"),
    EXPRESS(10, "快递"),
    DOCUMENT(12, "文件"),
    CAKE(13, "蛋糕"),
    FLOWER(14, "鲜花"),
    DIGITAL(15, "数码"),
    CLOTHING(16, "服装"),
    AUTO_PARTS(17, "汽配"),
    JEWELRY(18, "珠宝"),
    PIZZA(20, "披萨"),
    CHINESE_FOOD(21, "中餐"),
    AQUATIC_PRODUCTS(22, "水产"),
    PERSONAL_DELIVERY(27, "专人直送"),
    MID_END_DRINKS(32, "中端饮品"),
    CONVENIENCE_STORE(33, "便利店"),
    BREAD_AND_PASTRY(34, "面包糕点"),
    HOT_POT(35, "火锅"),
    CERTIFICATE(36, "证照"),
    BBQ_AND_CRAWFISH(40, "烧烤小龙虾"),
    EXTERNAL_LANDING_DISTRIBUTION(41, "外部落地配"),
    NEW_YEAR_DINNER(44, "年夜饭"),
    TOBACCO_AND_ALCOHOL_STORE(47, "烟酒行"),
    ADULT_PRODUCTS(48, "成人用品"),
    PET_SUPPLIES(55, "宠物用品"),
    MOTHER_AND_BABY_PRODUCTS(56, "母婴用品"),
    COSMETICS(57, "美妆用品"),
    HOME_BUILDING_MATERIALS(58, "家居建材"),
    EYEWEAR_STORE(59, "眼镜行"),
    GRAPHIC_ADVERTISEMENT(60, "图文广告"),
    NUCLEIC_ACID(65, "核酸"),
    OTHER(99, "其他");

    private final int code;
    private final String name;

    ProductType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

