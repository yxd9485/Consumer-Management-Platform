<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function(){
		$.fn.confirm = function(content, callBack) {
			$("#modalConfirmDlg .modal-title").text("提示信息");
            $("#modalConfirmDlg .modal-body p").html(content);
            $("#modalConfirmDlg .modal-body p").css("display", "");
            $("#modalConfirmDlg #btnCancel").css("display", "");
            $("#modalConfirmDlg #btnSure").text("确定");
            $("#modalConfirmDlg #btnSure").unbind("click");
            $("#modalConfirmDlg #btnSure").on("click", function() {
            	$(this).unbind("click");
            	callBack();
            });
            
            // 延迟100毫秒定位弹框的位置
            setTimeout(function(){
                $("#modalConfirmDlg").modal("show");
                var dialogH = $("#modalConfirmDlg").height();
                if (dialogH > screen.height) {
                    if ($("#mainFrame", window.parent.parent.document).size() > 0) {
                        $("#modalConfirmDlg .modal-content").css("top", ($("#mainFrame", window.parent.parent.document).contents().scrollTop() + screen.height * 0.0) + "px");
                    }
                }
            }, 100);
		}
		
		$.fn.alert = function(content, callBack) {
            $("#modalAlertDlg .modal-title").text("提示信息");
            $("#modalAlertDlg .modal-body p").html(content);
            $("#modalAlertDlg .modal-body p").css("display", "");
            $("#modalAlertDlg #btnCancel").css("display", "none");
            $("#modalAlertDlg #btnSure").text("关 闭");
            $("#modalAlertDlg #btnSure").unbind("click");
            $("#modalAlertDlg #btnSure").on("click", function() {
                $(this).unbind("click");
                callBack();
            });
            
            // 延迟100毫秒定位弹框的位置
            setTimeout(function(){
                $("#modalAlertDlg").modal("show");
                var dialogH = $("#modalConfirmDlg").height();
                if (dialogH > screen.height) {
                	if ($("#mainFrame", window.parent.parent.document).size() > 0) {
	                    $("#modalConfirmDlg .modal-content").css("top", ($("#mainFrame", window.parent.parent.document).contents().scrollTop() + screen.height * 0.0) + "px");
                	}
                }
            }, 100);
        }
		
		// 弹出提示 
		var errMsg = "${errMsg}";
		if (errMsg != "") {
			$.fn.alert(errMsg);
		}
	});
</script>

<div class="modal fade" id="modalConfirmDlg" tabindex="-1" data-backdrop="static" role="dialog" style="z-index: 2048;">
  <div class="modal-dialog" role="document">
    <div class="modal-content" style="top:20%;">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" style="font-size: 14px;">提示信息</h4>
      </div>
      <div class="modal-body">
        <p style="font-size: 12px; overflow-y: auto; min-height: 50px; max-height: 300px;" >One fine body&hellip;</p>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnSure" class="btn btn-default btn-blue" data-dismiss="modal">确定</button>
        <button type="button" id="btnCancel" class="btn btn-radius3" data-dismiss="modal">取消</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="modalAlertDlg" tabindex="-1" data-backdrop="static" role="dialog" style="z-index: 2048;">
      <div class="modal-dialog" role="document">
        <div class="modal-content" style="top:20%;">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" style="font-size: 14px;">提示信息</h4>
          </div>
          <div class="modal-body">
            <p style="font-size: 12px;">One fine body&hellip;</p>
          </div>
          <div class="modal-footer">
            <button type="button" id="btnSure" class="btn btn-default btn-blue" data-dismiss="modal">确定</button>
            <button type="button" id="btnCancel" class="btn btn-radius3" data-dismiss="modal">取消</button>
          </div>
        </div>
      </div>
    </div>
