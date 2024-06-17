package com.dbt.framework.wechat.message.bean;

import java.io.Serializable;
/**
 * 小程序模板消息主数据
 * @author hanshimeng
 *
 */
public class MsgAppletData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private MsgValue keyword1;//字段1（小标题）
	private MsgValue keyword2;//字段2（大标题）
	private MsgValue keyword3;//字段3（日期）
	private MsgValue keyword4;//字段4（内容1）
	private MsgValue keyword5;//字段5（内容2）
	private MsgValue name1;
	private MsgValue amount2;
	private MsgValue amount3;
	private MsgValue character_string1;
	private MsgValue thing1;
	private MsgValue thing2;
	private MsgValue thing3;
	private MsgValue thing4;
	private MsgValue thing5;
	private MsgValue thing6;
	private MsgValue thing8;
	private MsgValue thing9;
	private MsgValue thing12;
	private MsgValue thing14;
	private MsgValue date3;
	private MsgValue date4;
	private MsgValue date5;
	private MsgValue date6;
	private MsgValue date7;
	private MsgValue number7;
	private MsgValue time2;
	private MsgValue time3;
	private MsgValue time7;
	private MsgValue phrase2;
	private MsgValue phrase8;
	private MsgValue thing11;
	
	public MsgAppletData(){}
	public MsgAppletData(MsgValue keyword1, MsgValue keyword2, 
			MsgValue keyword3, MsgValue keyword4, MsgValue keyword5){
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.keyword5 = keyword5;
		
	}
	public MsgValue getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(MsgValue keyword1) {
		this.keyword1 = keyword1;
	}
	public MsgValue getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(MsgValue keyword2) {
		this.keyword2 = keyword2;
	}
	public MsgValue getKeyword3() {
		return keyword3;
	}
	public void setKeyword3(MsgValue keyword3) {
		this.keyword3 = keyword3;
	}
	public MsgValue getKeyword4() {
		return keyword4;
	}
	public void setKeyword4(MsgValue keyword4) {
		this.keyword4 = keyword4;
	}
	public MsgValue getKeyword5() {
		return keyword5;
	}
	public void setKeyword5(MsgValue keyword5) {
		this.keyword5 = keyword5;
	}
    public MsgValue getName1() {
        return name1;
    }
    public void setName1(MsgValue name1) {
        this.name1 = name1;
    }
    public MsgValue getAmount2() {
        return amount2;
    }
    public void setAmount2(MsgValue amount2) {
        this.amount2 = amount2;
    }
    public MsgValue getThing8() {
        return thing8;
    }
    public void setThing8(MsgValue thing8) {
        this.thing8 = thing8;
    }
    public MsgValue getCharacter_string1() {
        return character_string1;
    }
    public void setCharacter_string1(MsgValue character_string1) {
        this.character_string1 = character_string1;
    }
    public MsgValue getThing2() {
        return thing2;
    }
    public void setThing2(MsgValue thing2) {
        this.thing2 = thing2;
    }
    public MsgValue getThing3() {
        return thing3;
    }
    public void setThing3(MsgValue thing3) {
        this.thing3 = thing3;
    }
	public MsgValue getDate4() {
		return date4;
	}
	public void setDate4(MsgValue date4) {
		this.date4 = date4;
	}
	public MsgValue getThing5() {
		return thing5;
	}
	public void setThing5(MsgValue thing5) {
		this.thing5 = thing5;
	}
    public MsgValue getDate5() {
        return date5;
    }
    public void setDate5(MsgValue date5) {
        this.date5 = date5;
    }
    public MsgValue getNumber7() {
        return number7;
    }
    public void setNumber7(MsgValue number7) {
        this.number7 = number7;
    }
    public MsgValue getThing6() {
        return thing6;
    }
    public void setThing6(MsgValue thing6) {
        this.thing6 = thing6;
    }
    public MsgValue getDate3() {
        return date3;
    }
    public void setDate3(MsgValue date3) {
        this.date3 = date3;
    }
	public MsgValue getThing1() {
		return thing1;
	}
	public void setThing1(MsgValue thing1) {
		this.thing1 = thing1;
	}
	public MsgValue getThing4() {
		return thing4;
	}
	public void setThing4(MsgValue thing4) {
		this.thing4 = thing4;
	}
	public MsgValue getTime2() {
		return time2;
	}
	public void setTime2(MsgValue time2) {
		this.time2 = time2;
	}
	public MsgValue getTime3() {
		return time3;
	}
	public void setTime3(MsgValue time3) {
		this.time3 = time3;
	}
	public MsgValue getThing9() {
		return thing9;
	}
	public void setThing9(MsgValue thing9) {
		this.thing9 = thing9;
	}
	public MsgValue getThing12() {
        return thing12;
    }
    public void setThing12(MsgValue thing12) {
        this.thing12 = thing12;
    }
    public MsgValue getThing14() {
		return thing14;
	}
	public void setThing14(MsgValue thing14) {
		this.thing14 = thing14;
	}
	public MsgValue getTime7() {
		return time7;
	}
	public void setTime7(MsgValue time7) {
		this.time7 = time7;
	}
	public MsgValue getDate6() {
		return date6;
	}
	public void setDate6(MsgValue date6) {
		this.date6 = date6;
	}
	public MsgValue getDate7() {
		return date7;
	}
	public void setDate7(MsgValue date7) {
		this.date7 = date7;
	}

	public MsgValue getPhrase2() {
		return phrase2;
	}

	public void setPhrase2(MsgValue phrase2) {
		this.phrase2 = phrase2;
	}

	public MsgValue getPhrase8() {
		return phrase8;
	}
	public void setPhrase8(MsgValue phrase8) {
		this.phrase8 = phrase8;
	}
	public MsgValue getThing11() {
		return thing11;
	}
	public void setThing11(MsgValue thing11) {
		this.thing11 = thing11;
	}

	public MsgValue getAmount3() {
		return amount3;
	}

	public void setAmount3(MsgValue amount3) {
		this.amount3 = amount3;
	}
}
