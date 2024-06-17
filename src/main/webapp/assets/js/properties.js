$(function(){
	//全选按钮绑定
	$(".table-checkable").find("th.checkbox-column :checkbox").on("change", function(){
		var checked = $(this).prop("checked");
		var tbody = $(this).parents("table").children("tbody");
		tbody.each(function(m, td){
			$(td).find(".checkbox-column").each(function(n, box){
				var element = $(":checkbox", $(box)).prop("checked", checked).trigger("change");
				if (element.hasClass("uniform")) {
					$.uniform.update(element);
				}
				$(box).closest("tr").toggleClass("checked", checked);
			});
		});
	});

	//checkbox事件绑定
	$(".table-checkable").find("td.checkbox-column :checkbox").on("change", function(){
		var checked = $(this).prop("checked");
		$(this).closest("tr").toggleClass("checked", checked);
	});

    //重置按钮
    $("button.btn_reset").click(function(){
        $("div.form-group").find(":input[name]").each(function(){
            $(this).val("");
        });
    });

	//查询
	$("button.btn-primary").click(function(){
		
		var values = "params=";
		$("div.form-group").find(":input[name]").each(function(){
			values += $(this).val() + ",";
		});
		values = values.substr(0, values.length-1);
		initTable(values);
	});

	//排序
	var odr = "";
	var typ = "";
	$("table.dt_table tr>th:gt(0)[order]").off();
	$("table.dt_table tr>th:gt(0)[order]").on("click", function(){
		var values = "params=";
		$("div.form-group").find(":input[name]").each(function(){
			values += $(this).val() + ",";
		});
		values = values.substr(0, values.length-1);
		
		var order = $(this).attr("order");
		
		var type = "";
		
		if(order == odr){
			type = (typ == "asc") ? "desc" : "asc";
			sorts = (typ == "asc") ? "sorting_desc" : "sorting_asc";
		} else {
			type = "asc";
			sorts = "sorting_asc";
		}
		
		values += "&order="+order+","+type;
		
		initTable(values, order, type);
		$(this).attr("class", sorts);
		odr = order;
		typ = type;
	});
});