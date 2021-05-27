jQuery(function(){var S=jQuery,V=S("#uploader"),F=S('<ul class="filelist"></ul>').appendTo(V.find(".queueList")),O=V.find(".statusBar"),B=O.find(".info"),Q=V.find(".uploadBtn"),H=V.find(".placeholder"),L=O.find(".progress").hide(),N=0,R=0,P=window.devicePixelRatio||1,I=110*P,T=110*P,G="pedding",E={},C=(function(){var X=document.createElement("p").style,W="transition" in X||"WebkitTransition" in X||"MozTransition" in X||"msTransition" in X||"OTransition" in X;X=null;return W})(),K;if(!WebUploader.Uploader.support()){alert("Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器");throw new Error("WebUploader does not support the browser you are using.")}K=WebUploader.create({pick:{id:"#filePicker",label:"点击选择图片"},dnd:"#uploader .queueList",paste:document.body,accept:{title:"Images",extensions:"gif,jpg,jpeg,bmp,png",mimeTypes:"image/*"},swf:BASE_URL+"/Uploader.swf",disableGlobalDnd:true,chunked:true,server:"http://2betop.net/fileupload.php",fileNumLimit:300,fileSizeLimit:5*1024*1024,fileSingleSizeLimit:1*1024*1024});K.addButton({id:"#filePicker2",label:"继续添加"});function J(b){var c=S('<li id="'+b.id+'"><p class="title">'+b.name+'</p><p class="imgWrap"></p><p class="progress"><span></span></p></li>'),W=S('<div class="file-panel"><span class="cancel">删除</span><span class="rotateRight">向右旋转</span><span class="rotateLeft">向左旋转</span></div>').appendTo(c),Y=c.find("p.progress span"),X=c.find("p.imgWrap"),a=S('<p class="error"></p>'),Z=function(d){switch(d){case"exceed_size":text="文件大小超出";break;case"interrupt":text="上传暂停";break;default:text="上传失败，请重试";break}a.text(text).appendTo(c)};if(b.getStatus()==="invalid"){Z(b.statusText)}else{X.text("预览中");K.makeThumb(b,function(e,d){if(e){X.text("不能预览");return}var f=S('<img src="'+d+'">');X.empty().append(f)},I,T);E[b.id]=[b.size,0];b.rotation=0}b.on("statuschange",function(d,e){if(e==="progress"){Y.hide().width(0)}else{if(e==="queued"){c.off("mouseenter mouseleave");W.remove()}}if(d==="error"||d==="invalid"){console.log(b.statusText);Z(b.statusText);E[b.id][1]=1}else{if(d==="interrupt"){Z("interrupt")}else{if(d==="queued"){E[b.id][1]=0}else{if(d==="progress"){a.remove();Y.css("display","block")}else{if(d==="complete"){c.append('<span class="success"></span>')}}}}}c.removeClass("state-"+e).addClass("state-"+d)});c.on("mouseenter",function(){W.stop().animate({height:30})});c.on("mouseleave",function(){W.stop().animate({height:0})});W.on("click","span",function(){var d=S(this).index(),e;switch(d){case 0:K.removeFile(b);return;case 1:b.rotation+=90;break;case 2:b.rotation-=90;break}if(C){e="rotate("+b.rotation+"deg)";X.css({"-webkit-transform":e,"-mos-transform":e,"-o-transform":e,"transform":e})}else{X.css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation="+(~~((b.rotation/90)%4+4)%4)+")")}});c.appendTo(F)}function U(W){var X=S("#"+W.id);delete E[W.id];M();X.off().find(".file-panel").off().end().remove()}function M(){var Z=0,X=0,Y=L.children(),W;S.each(E,function(b,a){X+=a[0];Z+=a[0]*a[1]});W=X?Z/X:0;Y.eq(0).text(Math.round(W*100)+"%");Y.eq(1).css("width",Math.round(W*100)+"%");A()}function A(){var X="",W;if(G==="ready"){X="选中"+N+"张图片，共"+WebUploader.formatSize(R)+"。"}else{if(G==="confirm"){W=K.getStats();if(W.uploadFailNum){X="已成功上传"+W.successNum+"张照片至XX相册，"+W.uploadFailNum+'张照片上传失败，<a class="retry" href="#">重新上传</a>失败图片或<a class="ignore" href="#">忽略</a>'}}else{W=K.getStats();X="共"+N+"张（"+WebUploader.formatSize(R)+"），已上传"+W.successNum+"张";if(W.uploadFailNum){X+="，失败"+W.uploadFailNum+"张"}}}B.html(X)}function D(X){var Y,W;if(X===G){return}Q.removeClass("state-"+G);Q.addClass("state-"+X);G=X;switch(G){case"pedding":H.removeClass("element-invisible");F.parent().removeClass("filled");F.hide();O.addClass("element-invisible");K.refresh();break;case"ready":H.addClass("element-invisible");S("#filePicker2").removeClass("element-invisible");F.parent().addClass("filled");F.show();O.removeClass("element-invisible");K.refresh();break;case"uploading":S("#filePicker2").addClass("element-invisible");L.show();Q.text("暂停上传");break;case"paused":L.show();Q.text("继续上传");break;case"confirm":L.hide();Q.text("开始上传").addClass("disabled");W=K.getStats();if(W.successNum&&!W.uploadFailNum){D("finish");return}break;case"finish":W=K.getStats();if(W.successNum){alert("上传成功")}else{G="done";location.reload()}break}A()}K.onUploadProgress=function(Y,X){var Z=S("#"+Y.id),W=Z.find(".progress span");W.css("width",X*100+"%");E[Y.id][1]=X;M()};K.onFileQueued=function(W){N++;R+=W.size;if(N===1){H.addClass("element-invisible");O.show()}J(W);D("ready");M()};K.onFileDequeued=function(W){N--;R-=W.size;if(!N){D("pedding")}U(W);M()};K.on("all",function(W){var X;switch(W){case"uploadFinished":D("confirm");break;case"startUpload":D("uploading");break;case"stopUpload":D("paused");break}});K.onError=function(W){alert("Eroor: "+W)};Q.on("click",function(){if(S(this).hasClass("disabled")){return false}if(G==="ready"){K.upload()}else{if(G==="paused"){K.upload()}else{if(G==="uploading"){K.stop()}}}});B.on("click",".retry",function(){K.retry()});B.on("click",".ignore",function(){alert("todo")});Q.addClass("state-"+G);M()});