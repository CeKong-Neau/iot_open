(function(A,B){if(typeof define==="function"&&define.amd){define("simple-uploader",["jquery","simple-module"],function(D,C){return(A.returnExportsGlobal=B(D,C))})}else{if(typeof exports==="object"){module.exports=B(require("jquery"),require("simple-module"))}else{A.simple=A.simple||{};A.simple["uploader"]=B(jQuery,SimpleModule)}}}(this,function(F,D){var B,C,E={}.hasOwnProperty,A=function(H,I){for(var G in I){if(E.call(I,G)){H[G]=I[G]}}function J(){this.constructor=H}J.prototype=I.prototype;H.prototype=new J();H.__super__=I.prototype;return H};B=(function(H){A(G,H);function G(){return G.__super__.constructor.apply(this,arguments)}G.count=0;G.prototype.opts={url:"",params:null,fileKey:"upload_file",connectionCount:3};G.prototype._init=function(){this.files=[];this.queue=[];this.id=++G.count;this.on("uploadcomplete",(function(I){return function(J,K){I.files.splice(F.inArray(K,I.files),1);if(I.queue.length>0&&I.files.length<I.opts.connectionCount){return I.upload(I.queue.shift())}else{return I.uploading=false}}})(this));return F(window).on("beforeunload.uploader-"+this.id,(function(I){return function(J){if(!I.uploading){return}J.originalEvent.returnValue=I._t("leaveConfirm");return I._t("leaveConfirm")}})(this))};G.prototype.generateId=(function(){var I;I=0;return function(){return I+=1}})();G.prototype.upload=function(M,J){var N,I,L,K;if(J==null){J={}}if(M==null){return}if(F.isArray(M)){for(L=0,K=M.length;L<K;L++){N=M[L];this.upload(N,J)}}else{if(F(M).is("input:file")){I=F(M).attr("name");if(I){J.fileKey=I}this.upload(F.makeArray(F(M)[0].files),J)}else{if(!M.id||!M.obj){M=this.getFile(M)}}}if(!(M&&M.obj)){return}F.extend(M,J);if(this.files.length>=this.opts.connectionCount){this.queue.push(M);return}if(this.triggerHandler("beforeupload",[M])===false){return}this.files.push(M);this._xhrUpload(M);return this.uploading=true};G.prototype.getFile=function(K){var J,L,I;if(K instanceof window.File||K instanceof window.Blob){J=(L=K.fileName)!=null?L:K.name}else{return null}return{id:this.generateId(),url:this.opts.url,params:this.opts.params,fileKey:this.opts.fileKey,name:J,size:(I=K.fileSize)!=null?I:K.size,ext:J?J.split(".").pop().toLowerCase():"",obj:K}};G.prototype._xhrUpload=function(L){var J,M,K,I;J=new FormData();J.append(L.fileKey,L.obj);J.append("original_filename",L.name);if(L.params){I=L.params;for(M in I){K=I[M];J.append(M,K)}}return L.xhr=F.ajax({url:L.url,data:J,dataType:"json",processData:false,contentType:false,type:"POST",headers:{"X-File-Name":encodeURIComponent(L.name)},xhr:function(){var N;N=F.ajaxSettings.xhr();if(N){N.upload.onprogress=(function(O){return function(P){return O.progress(P)}})(this)}return N},progress:(function(N){return function(O){if(!O.lengthComputable){return}return N.trigger("uploadprogress",[L,O.loaded,O.total])}})(this),error:(function(N){return function(P,O,Q){return N.trigger("uploaderror",[L,P,O])}})(this),success:(function(N){return function(O){N.trigger("uploadprogress",[L,L.size,L.size]);return N.trigger("uploadsuccess",[L,O])}})(this),complete:(function(N){return function(P,O){return N.trigger("uploadcomplete",[L,P.responseText])}})(this)})};G.prototype.cancel=function(L){var M,I,K,J;if(!L.id){J=this.files;for(I=0,K=J.length;I<K;I++){M=J[I];if(M.id===L*1){L=M;break}}}this.trigger("uploadcancel",[L]);if(L.xhr){L.xhr.abort()}return L.xhr=null};G.prototype.readImageFile=function(L,I){var J,K;if(!F.isFunction(I)){return}K=new Image();K.onload=function(){return I(K)};K.onerror=function(){return I()};if(window.FileReader&&FileReader.prototype.readAsDataURL&&/^image/.test(L.type)){J=new FileReader();J.onload=function(M){return K.src=M.target.result};return J.readAsDataURL(L)}else{return I()}};G.prototype.destroy=function(){var K,I,L,J;this.queue.length=0;J=this.files;for(I=0,L=J.length;I<L;I++){K=J[I];this.cancel(K)}F(window).off(".uploader-"+this.id);return F(document).off(".uploader-"+this.id)};G.i18n={"zh-CN":{leaveConfirm:"正在上传文件，如果离开上传会自动取消"}};G.locale="zh-CN";return G})(D);C=function(G){return new B(G)};return C}));