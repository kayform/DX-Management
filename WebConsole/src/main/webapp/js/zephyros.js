var zephyros = {};

function pushMessage(t, msg){
    var mes = 'Message|' + msg;
    $.Notify({
        caption: mes.split("|")[0],
        content: mes.split("|")[1],
        type: t
    });
}

function showDialog(id, content){
	var dialog = $("#"+id).data('dialog');
    if (!dialog.element.data('opened')) {
        if (dialog == undefined) {
            console.log('Element not contain role dialog! Please add attribute data-role="dialog" to element ' + el);
            return false;
        }

        if (content != undefined) {
        	dialog.setContent("<h3>" + "메시지" + "</h3>" + "<p>" + content + "</p>");
        }
        dialog.open();
    } else {
        dialog.close();
    }
}

zephyros.makeModal = function() {

	var options = arguments[0];
	
	var id = options.id;
	var title = options.title;
	var contents = options.contents;
	var closeText = options.closeText;
	var buttons = options.buttons;
	
	var html = "";
	//html += "<div class=\"modal color-6 modal-alert\" id=\""+id+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\" style=\"display: none;\">";
	html += "  <div class=\"modal-dialog\">";
	html += "    <div class=\"modal-content widget widget-3\">";
	html += "      <div class=\"modal-header widget-head\">";
	html += "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\""+closeText+"\"><span aria-hidden=\"true\">&times;</span></button>";
	html += "        <h4 class=\"modal-title\" id=\""+id+"Label\">"+title+"</h4>";
	html += "      </div>";
	html += "      <div class=\"modal-body widget-body\" id=\""+id+"-modal-body\">";
	html += contents;
	html += "      </div>";
	html += "      <div class=\"modal-footer widget-footer\">";
	if(buttons) {
		for(var i=0;i<buttons.length;i++) {
			html += "        <button type=\"button\" id=\""+id+"_btn"+i+"\" modal=\""+id+"\" class=\"btn btn-primary\">"+buttons[i].text+"</button>";
		}
	}
	html += "      </div>";
	html += "    </div>";
	html += "  </div>";
	//html += "</div>";
	$("#" + id).html(html);
	
	for(var i=0;i<buttons.length;i++) {
		$("#"+id+"_btn"+i).on("click", buttons[i].click);
	}
};

zephyros.alert = function () {

	var options = arguments[0];
	
	options.id = "alertModal";
	if(!options.title) {
		options.title = "메시지";
	}
	if(!options.closeText) {
		options.closeText = "닫기";
	}
	options.buttons = [{
			text : "닫기",
			click: function() {
				if($("#alertModal").data("options").close) {
					$("#alertModal").data("options").close();
				}
				$("#alertModal").modal("hide");
			}
	}];

	var id = options.id;

	$("#alertModal").remove();

	if($("#alertModal").length == 0) {
		$("#content").append("<div class=\"modal "+$("#content").attr("class")+"\" id=\"alertModal\" style=\"display: none;\"></div>");
	}

	this.makeModal(options);
	
	$("#alertModal").data("options", options);
	
	// CSS
	if(!options.width) {
		$("#alertModal").css("width","400px");
	} else {
		$("#alertModal").css("width",options.width+"px");
	}
	$("#alertModal").css({top:"50%",left:"50%",margin:"-"+($("#" + id).height() / 2)+"px 0 0 -"+($("#" + id).width() / 2)+"px"});
	$("#alertModal .modal-content.widget").css("margin",0);
	$("#alertModal .modal-header").css("border","0");
	$("#alertModal .modal-header h4").css("margin-top","10px");
	$("#alertModal .modal-footer").css("box-shadow","none");
	
	// SCROLLBAR
	$("#alertModal .modal-body").addClass($("#content").attr("class")+"-scrollbar");
	$("#alertModal .modal-body").css("scroll-y", "off");
	
	// SHOW
	if(options.parentModal) {
		$("#"+options.parentModal).modal("hide");
	}
	$("#alertModal").modal("show");
	return $("#alertModal");
};

zephyros.confirm = function() {
	var options = arguments[0];
	options.id = "confirmModal";
	
	if(!options.title) {
		options.title = "메시지";
	}
	if(!options.closeText) {
		options.closeText = "닫기";
	}
	
	options.buttons = [{
		text : "확인",
		click: function() {
				$("#confirmModal").data("options").ok();
				$("#confirmModal").modal("hide");
			}
		},{
			text : "취소",
			click: function() {
				if($("#confirmModal").data("options").cancel) {
					$("#confirmModal").data("options").cancel();
				}
				var options =$("#confirmModal").data("options");
				if(options.parentModal) {
					$("#"+options.parentModal).modal("show");
				}
				$("#confirmModal").modal("hide");
			}
		}
	];

	var id = options.id;
	
	$("#confirmModal").remove();

	if($("#confirmModal").length == 0) {
		$("#content").append("<div class=\"modal "+$("#content").attr("class")+"\" id=\"confirmModal\" style=\"display: none;\"></div>");
	}
	
	$("#confirmModal").data("options", options);

	this.makeModal(options);
	
	// CSS
	if(!options.width) {
		$("#confirmModal").css("width","400px");
	} else {
		$("#confirmModal").css("width",options.width+"px");
	}
	$("#confirmModal").css({top:"50%",left:"50%",margin:"-"+($("#" + id).height() / 2)+"px 0 0 -"+($("#" + id).width() / 2)+"px"});
	$("#confirmModal .modal-content.widget").css("margin",0);
	$("#confirmModal .modal-header").css("border","0");
	$("#confirmModal .modal-header h4").css("margin-top","10px");
	$("#confirmModal .modal-footer").css("box-shadow","none");

	// SHOW
	if(options.parentModal) {
		$("#"+options.parentModal).modal("hide");
	}
	$("#confirmModal").modal("show");
	return $("#confirmModal");
};

zephyros.alertError = function () {

	var options = arguments[0];
	
	options.id = "alertModal";
	
	if(!options.title) {
		options.title = "오류";
	}
	if(!options.closeText) {
		options.closeText = "닫기";
	}
	options.buttons = [{
			text : "닫기",
			click: function() {
				var options =$("#alertModal").data("options");
				if($("#alertModal").data("options").close) {
					$("#alertModal").data("options").close();
				}
				if(options.parentModal) {
					$("#"+options.parentModal).modal("show");
				}
				$("#alertModal").modal("hide");
			}
	}];

	var id = options.id;

	$("#alertModal").remove();

	if($("#alertModal").length == 0) {
		$("#content").append("<div class=\"modal "+$("#content").attr("class")+"\" id=\"alertModal\" style=\"display: none;\"></div>");
	}
	
	$("#alertModal").data("options", options);

	this.makeModal(options);
	
	// CSS
	if(!options.width) {
		$("#alertModal").css("width","400px");
	} else {
		$("#alertModal").css("width",options.width+"px");
	}
	$("#alertModal").css({top:"50%",left:"50%",margin:"-"+($("#" + id).height() / 2)+"px 0 0 -"+($("#" + id).width() / 2)+"px"});
	$("#alertModal .modal-content.widget").css("margin",0);
	$("#alertModal .modal-header").css("border","0");
	$("#alertModal .modal-header h4").css("margin-top","10px");
	$("#alertModal .modal-footer").css("box-shadow","none");
	
	if(options.parentModal) {
		$("#"+options.parentModal).modal("hide");
	}
	// SHOW
	$("#alertModal").modal("show");
	return $("#alertModal");
};

zephyros.dialog = function () {

	var options = arguments[0];
	
	if(!options.title) {
		options.title = "";
	}
	if(!options.closeText) {
		options.closeText = "닫기";
	}
	if(!options.buttons) {
		options.buttons = [{
				text : "닫기",
				click: function() {
					$("#" + $(this).data("modal-id")).modal("hide");
				}
		}];
	}

	var id = options.id;

	this.makeModal(options);
	
	// CSS
	if(!options.width) {
		$("#" + id).css("width","800px");
	} else {
		$("#" + id).css("width", options.width+"px");
	}
	$("#" + id + " .modal-content.widget").css("margin","0");
	$("#" + id + " .modal-header").css("border","none");
	$("#" + id + " .modal-header h4").css("margin-top","10px");
	$("#" + id + " .modal-footer").css("box-shadow","none");

	// SCROLLBAR
	$("#" + id + " .modal-body").addClass($("#content").attr("class")+"-scrollbar");
	$("#" + id + " .modal-body").css("scroll-y", "off");
	$("#" + id + " .modal-body").css("max-height", "none");
	
	$("#" + id).css({top:"50%",left:"50%",margin:"-"+($("#" + id).height() / 2)+"px 0 0 -"+($("#" + id).width() / 2)+"px"});
	
	//console.log("$(\"#\" + id).height() > $(window).height() - 20 ==> " + ($("#" + id).height() > $(window).height() - 20));
	//console.log("$(window).height() - $(\"#myDialog .modal-header\").outerHeight() - $(\"#myDialog .modal-footer\").outerHeight() - 50 ==> " + $(window).height() + " - " + $("#myDialog .modal-footer").outerHeight() + " - 50");
	
	if($("#" + id).height() > $(window).height() - 20) {
		$("#" + id + " .modal-body").css("max-height", $(window).height() - $("#" + id + " .modal-header").outerHeight() - $("#" + id + " .modal-footer").outerHeight() - 50);
	} else {
		//console.log($(window).height() + " - " + $("#myDialog .modal-footer").outerHeight() + " - 50");
		$("#" + id + " .modal-body").css("max-height", $("#" + id).height() - $("#" + id + " .modal-header").outerHeight() - $("#" + id + " .modal-footer").outerHeight() - 30);
	}
	$("#" + id).css({top:"50%",left:"50%",margin:"-"+($("#" + id).height() / 2)+"px 0 0 -"+($("#" + id).width() / 2)+"px"});
	
	// EVENT
	
	// SHOW
	return $("#" + id);
};

zephyros.loading = {
		display: "none",
		show : function() {
			var id = arguments[0] ? "#" + arguments[0] : "body";
			$("body").data("loading-parent-id", id);
			$("body").data("loading-display", true);
			$(id).append("<div class=\"loading-overlay\">");
			// 1초간 설정이 없을 경우
			setTimeout( function() {
				if($("body").data("loading-display")) {
					var id = $("body").data("loading-parent-id");
					$(id).append("</div><div class=\"loading bar\"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>");
					if(!$("body").data("loading-display")) {
						zephyros.loading.hide(id);
					}
				};
			}, 300);
		},
		hide : function() {
			$("body").data("loading-display", false);
			$(".loading").remove();
			$(".loading-overlay").remove();
		}
};
	

function getCalendar(formName, startDate, endDate){
	if ($('#'+startDate).length) {
		$('#'+startDate).datepicker({
			showOtherMonths:true
			,dateFormat:"yy-mm-dd"
			,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
			,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
			,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일']
			,dayNamesShort: ['일','월','화','수','목','금','토']
			,dayNamesMin: ['일','월','화','수','목','금','토']
		  	,weekHeader: 'WK'
			,firstDay: 0
			,isRTL: false
			,showAnim:'show'
			,showMonthAfterYear: true
		  	,yearSuffix: '년'
		});
	}
	
	if ($('#'+endDate).length) {
		$('#'+endDate).datepicker({
			showOtherMonths:true
			,dateFormat:"yy-mm-dd"
			,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
			,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
			,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일']
			,dayNamesShort: ['일','월','화','수','목','금','토']
			,dayNamesMin: ['일','월','화','수','목','금','토']
		  	,weekHeader: 'WK'
			,firstDay: 0
			,isRTL: false
			,showAnim:'show'
			,showMonthAfterYear: true
		  	,yearSuffix: '년'
		});
		
		
	}
	
	/*
	var today = $.datepicker.formatDate($.datepicker.ATOM, new Date());
	var term = 7;
	var nowdate = new Date();
	var tomorrow = new Date(nowdate);
	tomorrow.setDate(nowdate.getDate()-term);
	var sevenDayAgo = $.datepicker.formatDate($.datepicker.ATOM, tomorrow);
	
	if ($('#'+startDate).val().trim() =='' || $('#'+startDate).val().trim()==null){
		$('#'+startDate).val(sevenDayAgo);	
	}
	
	if ($('#'+endDate).val().trim()==''|| $('#'+endDate).val().trim()==null){
		$('#'+endDate).val(today);	
	}
	 */
	 
	
	//var validator = $('#'+formName).validate();
	//validator.settings.rules[startDate] = { searchDateCheck : true, patternCheck : /^(19[7-9][0-9]|20\d{2})\/(0[0-9]|1[0-2])\/(0[1-9]|[1-2][0-9]|3[0-1])$/i};
	//validator.settings.rules[endDate] = { patternCheck : /^(19[7-9][0-9]|20\d{2})\/(0[0-9]|1[0-2])\/(0[1-9]|[1-2][0-9]|3[0-1])$/i};	
	
	
}

function ajaxErrorHandler(e, parentModalId) {
	//console.log();
	if(parentModalId == null || parentModalId == "") {
		if(e.status == 0) {
			zephyros.alertError({contents:'서버에 연결할 수 없습니다.'});
		} else if(e.status == 401) {
			zephyros.alertError({contents:'세션이 만료되었습니다.', close : function() {location.reload();}});
		}
	} else {
		if(e.status == 0) {
			zephyros.alertError({parentModal: parentModalId, contents:'서버에 연결할 수 없습니다.'});
		} else if(e.status == 401) {
			zephyros.alertError({parentModal: parentModalId, contents:'세션이 만료되었습니다.', close : function() {location.reload();}});
		}
	}
}

function runProgram(path)
{
    var shell = new ActiveXObject("WScript.Shell");
    var replace_path = "\"" + path.replace(/\\/gi, "\\") + "\" ";
    shell.Run(replace_path);
}

function existsFile(path, fileName)
{
	if (path == '') {
		return true;
	}
	
    var shell = new ActiveXObject("Scripting.FileSystemObject");
    //var replace_path = path.replace(/\\/gi, "\\");
    
    if (!shell.FileExists(path)) {
    	//alert("해당 경로에 파일이 존재하지 않습니다.")
    	return false;
    } else if (shell.GetFileName(path) != fileName) {
    	return false;
    }else {
    	return true;
    }
}