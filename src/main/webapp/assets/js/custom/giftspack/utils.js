/* 将数字以千分位分割 */
function thousandth(num){
	num = unThousandth(num);
	var regexStr = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g;
	return num.replace(regexStr, "$1,");
}

/* 将千分位分割的数字转换为普通数字 */
function unThousandth(num){
	return (num + "").replace(/,/g, "");
}