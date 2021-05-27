(function(){var E,H,F,I,G=[].slice,A=function(J,K){return function(){return J.apply(K,arguments)}},C={}.hasOwnProperty,B=function(K,L){for(var J in L){if(C.call(L,J)){K[J]=L[J]}}function M(){this.constructor=K}M.prototype=L.prototype;K.prototype=new M();K.__super__=L.prototype;return K},D=[].indexOf||function(L){for(var J=0,K=this.length;J<K;J++){if(J in this&&this[J]===L){return J}}return -1};H=window.Morris={};E=jQuery;H.EventEmitter=(function(){function J(){}J.prototype.on=function(L,K){if(this.handlers==null){this.handlers={}}if(this.handlers[L]==null){this.handlers[L]=[]}this.handlers[L].push(K);return this};J.prototype.fire=function(){var Q,M,N,L,K,P,O;N=arguments[0],Q=2<=arguments.length?G.call(arguments,1):[];if((this.handlers!=null)&&(this.handlers[N]!=null)){P=this.handlers[N];O=[];for(L=0,K=P.length;L<K;L++){M=P[L];O.push(M.apply(null,Q))}return O}};return J})();H.commas=function(L){var J,M,N,K;if(L!=null){N=L<0?"-":"";J=Math.abs(L);M=Math.floor(J).toFixed(0);N+=M.replace(/(?=(?:\d{3})+$)(?!^)/g,",");K=J.toString();if(K.length>M.length){N+=K.slice(M.length)}return N}else{return"-"}};H.pad2=function(J){return(J<10?"0":"")+J};H.Grid=(function(J){B(K,J);function K(M){this.resizeHandler=A(this.resizeHandler,this);var L=this;if(typeof M.element==="string"){this.el=E(document.getElementById(M.element))}else{this.el=E(M.element)}if((this.el==null)||this.el.length===0){throw new Error("Graph container element not found")}if(this.el.css("position")==="static"){this.el.css("position","relative")}this.options=E.extend({},this.gridDefaults,this.defaults||{},M);if(typeof this.options.units==="string"){this.options.postUnits=M.units}this.raphael=new Raphael(this.el[0]);this.elementWidth=null;this.elementHeight=null;this.dirty=false;this.selectFrom=null;if(this.init){this.init()}this.setData(this.options.data);this.el.bind("mousemove",function(P){var O,N,Q,S,R;N=L.el.offset();R=P.pageX-N.left;if(L.selectFrom){O=L.data[L.hitTest(Math.min(R,L.selectFrom))]._x;Q=L.data[L.hitTest(Math.max(R,L.selectFrom))]._x;S=Q-O;return L.selectionRect.attr({x:O,width:S})}else{return L.fire("hovermove",R,P.pageY-N.top)}});this.el.bind("mouseleave",function(N){if(L.selectFrom){L.selectionRect.hide();L.selectFrom=null}return L.fire("hoverout")});this.el.bind("touchstart touchmove touchend",function(O){var N,P;P=O.originalEvent.touches[0]||O.originalEvent.changedTouches[0];N=L.el.offset();L.fire("hover",P.pageX-N.left,P.pageY-N.top);return P});this.el.bind("click",function(O){var N;N=L.el.offset();return L.fire("gridclick",O.pageX-N.left,O.pageY-N.top)});if(this.options.rangeSelect){this.selectionRect=this.raphael.rect(0,0,0,this.el.innerHeight()).attr({fill:this.options.rangeSelectColor,stroke:false}).toBack().hide();this.el.bind("mousedown",function(O){var N;N=L.el.offset();return L.startRange(O.pageX-N.left)});this.el.bind("mouseup",function(O){var N;N=L.el.offset();L.endRange(O.pageX-N.left);return L.fire("hovermove",O.pageX-N.left,O.pageY-N.top)})}if(this.options.resize){E(window).bind("resize",function(N){if(L.timeoutId!=null){window.clearTimeout(L.timeoutId)}return L.timeoutId=window.setTimeout(L.resizeHandler,100)})}if(this.postInit){this.postInit()}}K.prototype.gridDefaults={dateFormat:null,axes:true,grid:true,gridLineColor:"#aaa",gridStrokeWidth:0.5,gridTextColor:"#888",gridTextSize:12,gridTextFamily:"sans-serif",gridTextWeight:"normal",hideHover:false,yLabelFormat:null,xLabelAngle:0,numLines:5,padding:25,parseTime:true,postUnits:"",preUnits:"",ymax:"auto",ymin:"auto 0",goals:[],goalStrokeWidth:1,goalLineColors:["#666633","#999966","#cc6666","#663333"],events:[],eventStrokeWidth:1,eventLineColors:["#005a04","#ccffbb","#3a5f0b","#005502"],rangeSelect:null,rangeSelectColor:"#eef",resize:false};K.prototype.setData=function(W,P){var R,a,O,U,b,Z,T,N,V,S,M,Y,L,X,Q;if(P==null){P=true}this.options.data=W;if((W==null)||W.length===0){this.data=[];this.raphael.clear();if(this.hover!=null){this.hover.hide()}return}Y=this.cumulative?0:null;L=this.cumulative?0:null;if(this.options.goals.length>0){b=Math.min.apply(Math,this.options.goals);U=Math.max.apply(Math,this.options.goals);L=L!=null?Math.min(L,b):b;Y=Y!=null?Math.max(Y,U):U}this.data=(function(){var c,e,d;d=[];for(O=c=0,e=W.length;c<e;O=++c){T=W[O];Z={src:T};Z.label=T[this.options.xkey];if(this.options.parseTime){Z.x=H.parseDate(Z.label);if(this.options.dateFormat){Z.label=this.options.dateFormat(Z.x)}else{if(typeof Z.label==="number"){Z.label=new Date(Z.label).toString()}}}else{Z.x=O;if(this.options.xLabelFormat){Z.label=this.options.xLabelFormat(Z)}}V=0;Z.y=(function(){var g,h,i,f;i=this.options.ykeys;f=[];for(a=g=0,h=i.length;g<h;a=++g){M=i[a];X=T[M];if(typeof X==="string"){X=parseFloat(X)}if((X!=null)&&typeof X!=="number"){X=null}if(X!=null){if(this.cumulative){V+=X}else{if(Y!=null){Y=Math.max(X,Y);L=Math.min(X,L)}else{Y=L=X}}}if(this.cumulative&&(V!=null)){Y=Math.max(V,Y);L=Math.min(V,L)}f.push(X)}return f}).call(this);d.push(Z)}return d}).call(this);if(this.options.parseTime){this.data=this.data.sort(function(d,c){return(d.x>c.x)-(c.x>d.x)})}this.xmin=this.data[0].x;this.xmax=this.data[this.data.length-1].x;this.events=[];if(this.options.events.length>0){if(this.options.parseTime){this.events=(function(){var d,f,e,c;e=this.options.events;c=[];for(d=0,f=e.length;d<f;d++){R=e[d];c.push(H.parseDate(R))}return c}).call(this)}else{this.events=this.options.events}this.xmax=Math.max(this.xmax,Math.max.apply(Math,this.events));this.xmin=Math.min(this.xmin,Math.min.apply(Math,this.events))}if(this.xmin===this.xmax){this.xmin-=1;this.xmax+=1}this.ymin=this.yboundary("min",L);this.ymax=this.yboundary("max",Y);if(this.ymin===this.ymax){if(L){this.ymin-=1}this.ymax+=1}if(((Q=this.options.axes)===true||Q==="both"||Q==="y")||this.options.grid===true){if(this.options.ymax===this.gridDefaults.ymax&&this.options.ymin===this.gridDefaults.ymin){this.grid=this.autoGridLines(this.ymin,this.ymax,this.options.numLines);this.ymin=Math.min(this.ymin,this.grid[0]);this.ymax=Math.max(this.ymax,this.grid[this.grid.length-1])}else{N=(this.ymax-this.ymin)/(this.options.numLines-1);this.grid=(function(){var c,d,e,f;f=[];for(S=c=d=this.ymin,e=this.ymax;N>0?c<=e:c>=e;S=c+=N){f.push(S)}return f}).call(this)}}this.dirty=true;if(P){return this.redraw()}};K.prototype.yboundary=function(L,O){var N,M;N=this.options["y"+L];if(typeof N==="string"){if(N.slice(0,4)==="auto"){if(N.length>5){M=parseInt(N.slice(5),10);if(O==null){return M}return Math[L](O,M)}else{if(O!=null){return O}else{return 0}}}else{return parseInt(N,10)}}else{return N}};K.prototype.autoGridLines=function(Q,T,L){var P,R,V,S,O,N,W,U,M;O=T-Q;M=Math.floor(Math.log(O)/Math.log(10));W=Math.pow(10,M);R=Math.floor(Q/W)*W;P=Math.ceil(T/W)*W;N=(P-R)/(L-1);if(W===1&&N>1&&Math.ceil(N)!==N){N=Math.ceil(N);P=R+N*(L-1)}if(R<0&&P>0){R=Math.floor(Q/N)*N;P=Math.ceil(T/N)*N}if(N<1){S=Math.floor(Math.log(N)/Math.log(10));V=(function(){var X,Y;Y=[];for(U=X=R;N>0?X<=P:X>=P;U=X+=N){Y.push(parseFloat(U.toFixed(1-S)))}return Y})()}else{V=(function(){var X,Y;Y=[];for(U=X=R;N>0?X<=P:X>=P;U=X+=N){Y.push(U)}return Y})()}return V};K.prototype._calc=function(){var M,O,P,Q,N,S,L,R;N=this.el.width();P=this.el.height();if(this.elementWidth!==N||this.elementHeight!==P||this.dirty){this.elementWidth=N;this.elementHeight=P;this.dirty=false;this.left=this.options.padding;this.right=this.elementWidth-this.options.padding;this.top=this.options.padding;this.bottom=this.elementHeight-this.options.padding;if((L=this.options.axes)===true||L==="both"||L==="y"){S=(function(){var T,W,U,V;U=this.grid;V=[];for(T=0,W=U.length;T<W;T++){O=U[T];V.push(this.measureText(this.yAxisFormat(O)).width)}return V}).call(this);this.left+=Math.max.apply(Math,S)}if((R=this.options.axes)===true||R==="both"||R==="x"){M=(function(){var T,U,V;V=[];for(Q=T=0,U=this.data.length;0<=U?T<U:T>U;Q=0<=U?++T:--T){V.push(this.measureText(this.data[Q].text,-this.options.xLabelAngle).height)}return V}).call(this);this.bottom-=Math.max.apply(Math,M)}this.width=Math.max(1,this.right-this.left);this.height=Math.max(1,this.bottom-this.top);this.dx=this.width/(this.xmax-this.xmin);this.dy=this.height/(this.ymax-this.ymin);if(this.calc){return this.calc()}}};K.prototype.transY=function(L){return this.bottom-(L-this.ymin)*this.dy};K.prototype.transX=function(L){if(this.data.length===1){return(this.left+this.right)/2}else{return this.left+(L-this.xmin)*this.dx}};K.prototype.redraw=function(){this.raphael.clear();this._calc();this.drawGrid();this.drawGoals();this.drawEvents();if(this.draw){return this.draw()}};K.prototype.measureText=function(L,O){var M,N;if(O==null){O=0}N=this.raphael.text(100,100,L).attr("font-size",this.options.gridTextSize).attr("font-family",this.options.gridTextFamily).attr("font-weight",this.options.gridTextWeight).rotate(O);M=N.getBBox();N.remove();return M};K.prototype.yAxisFormat=function(L){return this.yLabelFormat(L)};K.prototype.yLabelFormat=function(L){if(typeof this.options.yLabelFormat==="function"){return this.options.yLabelFormat(L)}else{return""+this.options.preUnits+(H.commas(L))+this.options.postUnits}};K.prototype.drawGrid=function(){var Q,S,N,M,L,O,P,R;if(this.options.grid===false&&((L=this.options.axes)!==true&&L!=="both"&&L!=="y")){return}O=this.grid;R=[];for(N=0,M=O.length;N<M;N++){Q=O[N];S=this.transY(Q);if((P=this.options.axes)===true||P==="both"||P==="y"){this.drawYAxisLabel(this.left-this.options.padding/2,S,this.yAxisFormat(Q))}if(this.options.grid){R.push(this.drawGridLine("M"+this.left+","+S+"H"+(this.left+this.width)))}else{R.push(void 0)}}return R};K.prototype.drawGoals=function(){var Q,L,M,N,R,O,P;O=this.options.goals;P=[];for(M=N=0,R=O.length;N<R;M=++N){L=O[M];Q=this.options.goalLineColors[M%this.options.goalLineColors.length];P.push(this.drawGoal(L,Q))}return P};K.prototype.drawEvents=function(){var Q,P,M,N,R,O,L;O=this.events;L=[];for(M=N=0,R=O.length;N<R;M=++N){P=O[M];Q=this.options.eventLineColors[M%this.options.eventLineColors.length];L.push(this.drawEvent(P,Q))}return L};K.prototype.drawGoal=function(L,M){return this.raphael.path("M"+this.left+","+(this.transY(L))+"H"+this.right).attr("stroke",M).attr("stroke-width",this.options.goalStrokeWidth)};K.prototype.drawEvent=function(L,M){return this.raphael.path("M"+(this.transX(L))+","+this.bottom+"V"+this.top).attr("stroke",M).attr("stroke-width",this.options.eventStrokeWidth)};K.prototype.drawYAxisLabel=function(L,N,M){return this.raphael.text(L,N,M).attr("font-size",this.options.gridTextSize).attr("font-family",this.options.gridTextFamily).attr("font-weight",this.options.gridTextWeight).attr("fill",this.options.gridTextColor).attr("text-anchor","end")};K.prototype.drawGridLine=function(L){return this.raphael.path(L).attr("stroke",this.options.gridLineColor).attr("stroke-width",this.options.gridStrokeWidth)};K.prototype.startRange=function(L){this.hover.hide();this.selectFrom=L;return this.selectionRect.attr({x:L,width:0}).show()};K.prototype.endRange=function(L){var M,N;if(this.selectFrom){N=Math.min(this.selectFrom,L);M=Math.max(this.selectFrom,L);this.options.rangeSelect.call(this.el,{start:this.data[this.hitTest(N)].x,end:this.data[this.hitTest(M)].x});return this.selectFrom=null}};K.prototype.resizeHandler=function(){this.timeoutId=null;this.raphael.setSize(this.el.width(),this.el.height());return this.redraw()};return K})(H.EventEmitter);H.parseDate=function(U){var S,R,N,O,P,L,K,M,J,T,Q;if(typeof U==="number"){return U}R=U.match(/^(\d+) Q(\d)$/);O=U.match(/^(\d+)-(\d+)$/);P=U.match(/^(\d+)-(\d+)-(\d+)$/);K=U.match(/^(\d+) W(\d+)$/);M=U.match(/^(\d+)-(\d+)-(\d+)[ T](\d+):(\d+)(Z|([+-])(\d\d):?(\d\d))?$/);J=U.match(/^(\d+)-(\d+)-(\d+)[ T](\d+):(\d+):(\d+(\.\d+)?)(Z|([+-])(\d\d):?(\d\d))?$/);if(R){return new Date(parseInt(R[1],10),parseInt(R[2],10)*3-1,1).getTime()}else{if(O){return new Date(parseInt(O[1],10),parseInt(O[2],10)-1,1).getTime()}else{if(P){return new Date(parseInt(P[1],10),parseInt(P[2],10)-1,parseInt(P[3],10)).getTime()}else{if(K){T=new Date(parseInt(K[1],10),0,1);if(T.getDay()!==4){T.setMonth(0,1+((4-T.getDay())+7)%7)}return T.getTime()+parseInt(K[2],10)*604800000}else{if(M){if(!M[6]){return new Date(parseInt(M[1],10),parseInt(M[2],10)-1,parseInt(M[3],10),parseInt(M[4],10),parseInt(M[5],10)).getTime()}else{L=0;if(M[6]!=="Z"){L=parseInt(M[8],10)*60+parseInt(M[9],10);if(M[7]==="+"){L=0-L}}return Date.UTC(parseInt(M[1],10),parseInt(M[2],10)-1,parseInt(M[3],10),parseInt(M[4],10),parseInt(M[5],10)+L)}}else{if(J){Q=parseFloat(J[6]);S=Math.floor(Q);N=Math.round((Q-S)*1000);if(!J[8]){return new Date(parseInt(J[1],10),parseInt(J[2],10)-1,parseInt(J[3],10),parseInt(J[4],10),parseInt(J[5],10),S,N).getTime()}else{L=0;if(J[8]!=="Z"){L=parseInt(J[10],10)*60+parseInt(J[11],10);if(J[9]==="+"){L=0-L}}return Date.UTC(parseInt(J[1],10),parseInt(J[2],10)-1,parseInt(J[3],10),parseInt(J[4],10),parseInt(J[5],10)+L,S,N)}}else{return new Date(parseInt(U,10),0,1).getTime()}}}}}}};H.Hover=(function(){J.defaults={"class":"morris-hover morris-default-style"};function J(K){if(K==null){K={}}this.options=E.extend({},H.Hover.defaults,K);this.el=E("<div class='"+this.options["class"]+"'></div>");this.el.hide();this.options.parent.append(this.el)}J.prototype.update=function(K,L,M){this.html;this.show();return this.moveTo(L,M)};J.prototype.html=function(K){return this.el.html(K)};J.prototype.moveTo=function(Q,R){var K,P,O,L,M,N;M=this.options.parent.innerWidth();L=this.options.parent.innerHeight();P=this.el.outerWidth();K=this.el.outerHeight();O=Math.min(Math.max(0,Q-P/2),M-P);if(R!=null){N=R-K-10;if(N<0){N=R+10;if(N+K>L){N=L/2-K/2}}}else{N=L/2-K/2}return this.el.css({left:O+"px",top:parseInt(N)+"px"})};J.prototype.show=function(){return this.el.show()};J.prototype.hide=function(){return this.el.hide()};return J})();H.Line=(function(K){B(J,K);function J(L){this.hilight=A(this.hilight,this);this.onHoverOut=A(this.onHoverOut,this);this.onHoverMove=A(this.onHoverMove,this);this.onGridClick=A(this.onGridClick,this);if(!(this instanceof H.Line)){return new H.Line(L)}J.__super__.constructor.call(this,L)}J.prototype.init=function(){if(this.options.hideHover!=="always"){this.hover=new H.Hover({parent:this.el});this.on("hovermove",this.onHoverMove);this.on("hoverout",this.onHoverOut);return this.on("gridclick",this.onGridClick)}};J.prototype.defaults={lineWidth:3,pointSize:4,lineColors:["#0b62a4","#7A92A3","#4da74d","#afd8f8","#edc240","#cb4b4b","#9440ed"],pointStrokeWidths:[1],pointStrokeColors:["#ffffff"],pointFillColors:[],smooth:true,xLabels:"auto",xLabelFormat:null,xLabelMargin:24,continuousLine:true,hideHover:false};J.prototype.calc=function(){this.calcPoints();return this.generatePaths()};J.prototype.calcPoints=function(){var O,P,M,Q,N,L;N=this.data;L=[];for(M=0,Q=N.length;M<Q;M++){O=N[M];O._x=this.transX(O.x);O._y=(function(){var T,U,S,R;S=O.y;R=[];for(T=0,U=S.length;T<U;T++){P=S[T];if(P!=null){R.push(this.transY(P))}else{R.push(P)}}return R}).call(this);L.push(O._ymax=Math.min.apply(Math,[this.bottom].concat((function(){var T,U,S,R;S=O._y;R=[];for(T=0,U=S.length;T<U;T++){P=S[T];if(P!=null){R.push(P)}}return R})())))}return L};J.prototype.hitTest=function(O){var P,L,M,Q,N;if(this.data.length===0){return null}N=this.data.slice(1);for(P=M=0,Q=N.length;M<Q;P=++M){L=N[P];if(O<(L._x+this.data[P]._x)/2){break}}return P};J.prototype.onGridClick=function(M,N){var L;L=this.hitTest(M);return this.fire("click",L,this.data[L].src,M,N)};J.prototype.onHoverMove=function(M,N){var L;L=this.hitTest(M);return this.displayHoverForRow(L)};J.prototype.onHoverOut=function(){if(this.options.hideHover!==false){return this.displayHoverForRow(null)}};J.prototype.displayHoverForRow=function(M){var L;if(M!=null){(L=this.hover).update.apply(L,this.hoverContentForRow(M));return this.hilight(M)}else{this.hover.hide();return this.hilight()}};J.prototype.hoverContentForRow=function(L){var S,P,Q,R,O,N,M;Q=this.data[L];S="<div class='morris-hover-row-label'>"+Q.label+"</div>";M=Q.y;for(P=O=0,N=M.length;O<N;P=++O){R=M[P];S+="<div class='morris-hover-point' style='color: "+(this.colorFor(Q,P,"label"))+"'>\n  "+this.options.labels[P]+":\n  "+(this.yLabelFormat(R))+"\n</div>"}if(typeof this.options.hoverCallback==="function"){S=this.options.hoverCallback(L,this.options,S,Q.src)}return[S,Q._x,Q._ymax]};J.prototype.generatePaths=function(){var P,N,M,O,L;return this.paths=(function(){var Q,S,R,T;T=[];for(M=Q=0,S=this.options.ykeys.length;0<=S?Q<S:Q>S;M=0<=S?++Q:--Q){L=typeof this.options.smooth==="boolean"?this.options.smooth:(R=this.options.ykeys[M],D.call(this.options.smooth,R)>=0);N=(function(){var W,X,V,U;V=this.data;U=[];for(W=0,X=V.length;W<X;W++){O=V[W];if(O._y[M]!==void 0){U.push({x:O._x,y:O._y[M]})}}return U}).call(this);if(this.options.continuousLine){N=(function(){var V,W,U;U=[];for(V=0,W=N.length;V<W;V++){P=N[V];if(P.y!==null){U.push(P)}}return U})()}if(N.length>1){T.push(H.Line.createPath(N,L,this.bottom))}else{T.push(null)}}return T}).call(this)};J.prototype.draw=function(){var L;if((L=this.options.axes)===true||L==="both"||L==="x"){this.drawXAxis()}this.drawSeries();if(this.options.hideHover===false){return this.displayHoverForRow(this.data.length-1)}};J.prototype.drawXAxis=function(){var O,Q,V,U,S,P,N,L,M,R,T=this;N=this.bottom+this.options.padding/2;S=null;U=null;O=function(c,Z){var W,a,X,b,Y;W=T.drawXAxisLabel(T.transX(Z),N,c);Y=W.getBBox();W.transform("r"+(-T.options.xLabelAngle));a=W.getBBox();W.transform("t0,"+(a.height/2)+"...");if(T.options.xLabelAngle!==0){b=-0.5*Y.width*Math.cos(T.options.xLabelAngle*Math.PI/180);W.transform("t"+b+",0...")}a=W.getBBox();if(((S==null)||S>=a.x+a.width||(U!=null)&&U>=a.x)&&a.x>=0&&(a.x+a.width)<T.el.width()){if(T.options.xLabelAngle!==0){X=1.25*T.options.gridTextSize/Math.sin(T.options.xLabelAngle*Math.PI/180);U=a.x-X}return S=a.x-T.options.xLabelMargin}else{return W.remove()}};if(this.options.parseTime){if(this.data.length===1&&this.options.xLabels==="auto"){V=[[this.data[0].label,this.data[0].x]]}else{V=H.labelSeries(this.xmin,this.xmax,this.width,this.options.xLabels,this.options.xLabelFormat)}}else{V=(function(){var X,Z,Y,W;Y=this.data;W=[];for(X=0,Z=Y.length;X<Z;X++){P=Y[X];W.push([P.label,P.x])}return W}).call(this)}V.reverse();R=[];for(L=0,M=V.length;L<M;L++){Q=V[L];R.push(O(Q[0],Q[1]))}return R};J.prototype.drawSeries=function(){var M,N,P,Q,O,L;this.seriesPoints=[];for(M=N=Q=this.options.ykeys.length-1;Q<=0?N<=0:N>=0;M=Q<=0?++N:--N){this._drawLineFor(M)}L=[];for(M=P=O=this.options.ykeys.length-1;O<=0?P<=0:P>=0;M=O<=0?++P:--P){L.push(this._drawPointFor(M))}return L};J.prototype._drawPointFor=function(Q){var L,O,M,R,N,P;this.seriesPoints[Q]=[];N=this.data;P=[];for(M=0,R=N.length;M<R;M++){O=N[M];L=null;if(O._y[Q]!=null){L=this.drawLinePoint(O._x,O._y[Q],this.colorFor(O,Q,"point"),Q)}P.push(this.seriesPoints[Q].push(L))}return P};J.prototype._drawLineFor=function(L){var M;M=this.paths[L];if(M!==null){return this.drawLinePath(M,this.colorFor(null,L,"line"),L)}};J.createPath=function(U,V,S){var T,R,W,O,M,Z,L,b,X,a,N,Y,P,Q;L="";if(V){W=H.Line.gradients(U)}b={y:null};for(O=P=0,Q=U.length;P<Q;O=++P){T=U[O];if(T.y!=null){if(b.y!=null){if(V){R=W[O];Z=W[O-1];M=(T.x-b.x)/4;X=b.x+M;N=Math.min(S,b.y+M*Z);a=T.x-M;Y=Math.min(S,T.y-M*R);L+="C"+X+","+N+","+a+","+Y+","+T.x+","+T.y}else{L+="L"+T.x+","+T.y}}else{if(!V||(W[O]!=null)){L+="M"+T.x+","+T.y}}}b=T}return L};J.gradients=function(S){var P,L,O,Q,T,N,M,R;L=function(V,U){return(V.y-U.y)/(V.x-U.x)};R=[];for(O=N=0,M=S.length;N<M;O=++N){P=S[O];if(P.y!=null){Q=S[O+1]||{y:null};T=S[O-1]||{y:null};if((T.y!=null)&&(Q.y!=null)){R.push(L(T,Q))}else{if(T.y!=null){R.push(L(T,P))}else{if(Q.y!=null){R.push(L(P,Q))}else{R.push(null)}}}}else{R.push(null)}}return R};J.prototype.hilight=function(P){var L,M,O,Q,N;if(this.prevHilight!==null&&this.prevHilight!==P){for(L=M=0,Q=this.seriesPoints.length-1;0<=Q?M<=Q:M>=Q;L=0<=Q?++M:--M){if(this.seriesPoints[L][this.prevHilight]){this.seriesPoints[L][this.prevHilight].animate(this.pointShrinkSeries(L))}}}if(P!==null&&this.prevHilight!==P){for(L=O=0,N=this.seriesPoints.length-1;0<=N?O<=N:O>=N;L=0<=N?++O:--O){if(this.seriesPoints[L][P]){this.seriesPoints[L][P].animate(this.pointGrowSeries(L))}}}return this.prevHilight=P};J.prototype.colorFor=function(M,N,L){if(typeof this.options.lineColors==="function"){return this.options.lineColors.call(this,M,N,L)}else{if(L==="point"){return this.options.pointFillColors[N%this.options.pointFillColors.length]||this.options.lineColors[N%this.options.lineColors.length]}else{return this.options.lineColors[N%this.options.lineColors.length]}}};J.prototype.drawXAxisLabel=function(L,N,M){return this.raphael.text(L,N,M).attr("font-size",this.options.gridTextSize).attr("font-family",this.options.gridTextFamily).attr("font-weight",this.options.gridTextWeight).attr("fill",this.options.gridTextColor)};J.prototype.drawLinePath=function(N,M,L){return this.raphael.path(N).attr("stroke",M).attr("stroke-width",this.lineWidthForSeries(L))};J.prototype.drawLinePoint=function(M,O,L,N){return this.raphael.circle(M,O,this.pointSizeForSeries(N)).attr("fill",L).attr("stroke-width",this.pointStrokeWidthForSeries(N)).attr("stroke",this.pointStrokeColorForSeries(N))};J.prototype.pointStrokeWidthForSeries=function(L){return this.options.pointStrokeWidths[L%this.options.pointStrokeWidths.length]};J.prototype.pointStrokeColorForSeries=function(L){return this.options.pointStrokeColors[L%this.options.pointStrokeColors.length]};J.prototype.lineWidthForSeries=function(L){if(this.options.lineWidth instanceof Array){return this.options.lineWidth[L%this.options.lineWidth.length]}else{return this.options.lineWidth}};J.prototype.pointSizeForSeries=function(L){if(this.options.pointSize instanceof Array){return this.options.pointSize[L%this.options.pointSize.length]}else{return this.options.pointSize}};J.prototype.pointGrowSeries=function(L){return Raphael.animation({r:this.pointSizeForSeries(L)+3},25,"linear")};J.prototype.pointShrinkSeries=function(L){return Raphael.animation({r:this.pointSizeForSeries(L)},25,"linear")};return J})(H.Grid);H.labelSeries=function(S,P,Y,U,R){var L,T,J,W,X,K,Q,N,O,V,M;J=200*(P-S)/Y;T=new Date(S);Q=H.LABEL_SPECS[U];if(Q===void 0){M=H.AUTO_LABEL_ORDER;for(O=0,V=M.length;O<V;O++){W=M[O];K=H.LABEL_SPECS[W];if(J>=K.span){Q=K;break}}}if(Q===void 0){Q=H.LABEL_SPECS["second"]}if(R){Q=E.extend({},Q,{fmt:R})}L=Q.start(T);X=[];while((N=L.getTime())<=P){if(N>=S){X.push([Q.fmt(L),N])}Q.incr(L)}return X};F=function(J){return{span:J*60*1000,start:function(K){return new Date(K.getFullYear(),K.getMonth(),K.getDate(),K.getHours())},fmt:function(K){return""+(H.pad2(K.getHours()))+":"+(H.pad2(K.getMinutes()))},incr:function(K){return K.setUTCMinutes(K.getUTCMinutes()+J)}}};I=function(J){return{span:J*1000,start:function(K){return new Date(K.getFullYear(),K.getMonth(),K.getDate(),K.getHours(),K.getMinutes())},fmt:function(K){return""+(H.pad2(K.getHours()))+":"+(H.pad2(K.getMinutes()))+":"+(H.pad2(K.getSeconds()))},incr:function(K){return K.setUTCSeconds(K.getUTCSeconds()+J)}}};H.LABEL_SPECS={"decade":{span:172800000000,start:function(J){return new Date(J.getFullYear()-J.getFullYear()%10,0,1)},fmt:function(J){return""+(J.getFullYear())},incr:function(J){return J.setFullYear(J.getFullYear()+10)}},"year":{span:17280000000,start:function(J){return new Date(J.getFullYear(),0,1)},fmt:function(J){return""+(J.getFullYear())},incr:function(J){return J.setFullYear(J.getFullYear()+1)}},"month":{span:2419200000,start:function(J){return new Date(J.getFullYear(),J.getMonth(),1)},fmt:function(J){return""+(J.getFullYear())+"-"+(H.pad2(J.getMonth()+1))},incr:function(J){return J.setMonth(J.getMonth()+1)}},"week":{span:604800000,start:function(J){return new Date(J.getFullYear(),J.getMonth(),J.getDate())},fmt:function(J){return""+(J.getFullYear())+"-"+(H.pad2(J.getMonth()+1))+"-"+(H.pad2(J.getDate()))},incr:function(J){return J.setDate(J.getDate()+7)}},"day":{span:86400000,start:function(J){return new Date(J.getFullYear(),J.getMonth(),J.getDate())},fmt:function(J){return""+(J.getFullYear())+"-"+(H.pad2(J.getMonth()+1))+"-"+(H.pad2(J.getDate()))},incr:function(J){return J.setDate(J.getDate()+1)}},"hour":F(60),"30min":F(30),"15min":F(15),"10min":F(10),"5min":F(5),"minute":F(1),"30sec":I(30),"15sec":I(15),"10sec":I(10),"5sec":I(5),"second":I(1)};H.AUTO_LABEL_ORDER=["decade","year","month","week","day","hour","30min","15min","10min","5min","minute","30sec","15sec","10sec","5sec","second"];H.Area=(function(K){var J;B(L,K);J={fillOpacity:"auto",behaveLikeLine:false};function L(N){var M;if(!(this instanceof H.Area)){return new H.Area(N)}M=E.extend({},J,N);this.cumulative=!M.behaveLikeLine;if(M.fillOpacity==="auto"){M.fillOpacity=M.behaveLikeLine?0.8:1}L.__super__.constructor.call(this,M)}L.prototype.calcPoints=function(){var Q,O,R,N,S,P,M;P=this.data;M=[];for(N=0,S=P.length;N<S;N++){Q=P[N];Q._x=this.transX(Q.x);O=0;Q._y=(function(){var V,W,U,T;U=Q.y;T=[];for(V=0,W=U.length;V<W;V++){R=U[V];if(this.options.behaveLikeLine){T.push(this.transY(R))}else{O+=R||0;T.push(this.transY(O))}}return T}).call(this);M.push(Q._ymax=Math.max.apply(Math,Q._y))}return M};L.prototype.drawSeries=function(){var T,W,M,S,Q,P,O,R,V,N,U;this.seriesPoints=[];if(this.options.behaveLikeLine){W=(function(){V=[];for(var X=0,Y=this.options.ykeys.length-1;0<=Y?X<=Y:X>=Y;0<=Y?X++:X--){V.push(X)}return V}).apply(this)}else{W=(function(){N=[];for(var X=R=this.options.ykeys.length-1;R<=0?X<=0:X>=0;R<=0?X++:X--){N.push(X)}return N}).apply(this)}U=[];for(Q=0,P=W.length;Q<P;Q++){T=W[Q];this._drawFillFor(T);this._drawLineFor(T);U.push(this._drawPointFor(T))}return U};L.prototype._drawFillFor=function(M){var N;N=this.paths[M];if(N!==null){N=N+("L"+(this.transX(this.xmax))+","+this.bottom+"L"+(this.transX(this.xmin))+","+this.bottom+"Z");return this.drawFilledPath(N,this.fillForSeries(M))}};L.prototype.fillForSeries=function(M){var N;N=Raphael.rgb2hsl(this.colorFor(this.data[M],M,"line"));return Raphael.hsl(N.h,this.options.behaveLikeLine?N.s*0.9:N.s*0.75,Math.min(0.98,this.options.behaveLikeLine?N.l*1.2:N.l*1.25))};L.prototype.drawFilledPath=function(N,M){return this.raphael.path(N).attr("fill",M).attr("fill-opacity",this.options.fillOpacity).attr("stroke","none")};return L})(H.Line);H.Bar=(function(K){B(J,K);function J(L){this.onHoverOut=A(this.onHoverOut,this);this.onHoverMove=A(this.onHoverMove,this);this.onGridClick=A(this.onGridClick,this);if(!(this instanceof H.Bar)){return new H.Bar(L)}J.__super__.constructor.call(this,E.extend({},L,{parseTime:false}))}J.prototype.init=function(){this.cumulative=this.options.stacked;if(this.options.hideHover!=="always"){this.hover=new H.Hover({parent:this.el});this.on("hovermove",this.onHoverMove);this.on("hoverout",this.onHoverOut);return this.on("gridclick",this.onGridClick)}};J.prototype.defaults={barSizeRatio:0.75,barGap:3,barColors:["#0b62a4","#7a92a3","#4da74d","#afd8f8","#edc240","#cb4b4b","#9440ed"],barOpacity:1,barRadius:[0,0,0,0],xLabelMargin:50};J.prototype.calc=function(){var L;this.calcBars();if(this.options.hideHover===false){return(L=this.hover).update.apply(L,this.hoverContentForRow(this.data.length-1))}};J.prototype.calcBars=function(){var P,O,Q,M,R,N,L;N=this.data;L=[];for(P=M=0,R=N.length;M<R;P=++M){O=N[P];O._x=this.left+this.width*(P+0.5)/this.data.length;L.push(O._y=(function(){var U,V,T,S;T=O.y;S=[];for(U=0,V=T.length;U<V;U++){Q=T[U];if(Q!=null){S.push(this.transY(Q))}else{S.push(null)}}return S}).call(this))}return L};J.prototype.draw=function(){var L;if((L=this.options.axes)===true||L==="both"||L==="x"){this.drawXAxis()}return this.drawSeries()};J.prototype.drawXAxis=function(){var P,S,N,L,M,R,U,Q,V,W,O,X,T;W=this.bottom+(this.options.xAxisLabelTopPadding||this.options.padding/2);U=null;R=null;T=[];for(P=O=0,X=this.data.length;0<=X?O<X:O>X;P=0<=X?++O:--O){Q=this.data[this.data.length-1-P];S=this.drawXAxisLabel(Q._x,W,Q.label);V=S.getBBox();S.transform("r"+(-this.options.xLabelAngle));N=S.getBBox();S.transform("t0,"+(N.height/2)+"...");if(this.options.xLabelAngle!==0){M=-0.5*V.width*Math.cos(this.options.xLabelAngle*Math.PI/180);S.transform("t"+M+",0...")}if(((U==null)||U>=N.x+N.width||(R!=null)&&R>=N.x)&&N.x>=0&&(N.x+N.width)<this.el.width()){if(this.options.xLabelAngle!==0){L=1.25*this.options.gridTextSize/Math.sin(this.options.xLabelAngle*Math.PI/180);R=N.x-L}T.push(U=N.x-this.options.xLabelMargin)}else{T.push(S.remove())}}return T};J.prototype.drawSeries=function(){var O,W,V,P,U,R,M,X,S,T,N,Q,Y,L;V=this.width/this.options.data.length;X=this.options.stacked!=null?1:this.options.ykeys.length;O=(V*this.options.barSizeRatio-this.options.barGap*(X-1))/X;M=V*(1-this.options.barSizeRatio)/2;L=this.ymin<=0&&this.ymax>=0?this.transY(0):null;return this.bars=(function(){var a,c,b,Z;b=this.data;Z=[];for(P=a=0,c=b.length;a<c;P=++a){S=b[P];U=0;Z.push((function(){var f,g,e,d;e=S._y;d=[];for(T=f=0,g=e.length;f<g;T=++f){Y=e[T];if(Y!==null){if(L){Q=Math.min(Y,L);W=Math.max(Y,L)}else{Q=Y;W=this.bottom}R=this.left+P*V+M;if(!this.options.stacked){R+=T*(O+this.options.barGap)}N=W-Q;if(this.options.stacked){Q-=U}this.drawBar(R,Q,O,N,this.colorFor(S,T,"bar"),this.options.barOpacity,this.options.barRadius);d.push(U+=N)}else{d.push(null)}}return d}).call(this))}return Z}).call(this)};J.prototype.colorFor=function(O,P,L){var M,N;if(typeof this.options.barColors==="function"){M={x:O.x,y:O.y[P],label:O.label};N={index:P,key:this.options.ykeys[P],label:this.options.labels[P]};return this.options.barColors.call(this,M,N,L)}else{return this.options.barColors[P%this.options.barColors.length]}};J.prototype.hitTest=function(L){if(this.data.length===0){return null}L=Math.max(Math.min(L,this.right),this.left);return Math.min(this.data.length-1,Math.floor((L-this.left)/(this.width/this.data.length)))};J.prototype.onGridClick=function(M,N){var L;L=this.hitTest(M);return this.fire("click",L,this.data[L].src,M,N)};J.prototype.onHoverMove=function(N,O){var L,M;L=this.hitTest(N);return(M=this.hover).update.apply(M,this.hoverContentForRow(L))};J.prototype.onHoverOut=function(){if(this.options.hideHover!==false){return this.hover.hide()}};J.prototype.hoverContentForRow=function(L){var T,P,Q,R,S,O,N,M;Q=this.data[L];T="<div class='morris-hover-row-label'>"+Q.label+"</div>";M=Q.y;for(P=O=0,N=M.length;O<N;P=++O){S=M[P];T+="<div class='morris-hover-point' style='color: "+(this.colorFor(Q,P,"label"))+"'>\n  "+this.options.labels[P]+":\n  "+(this.yLabelFormat(S))+"\n</div>"}if(typeof this.options.hoverCallback==="function"){T=this.options.hoverCallback(L,this.options,T,Q.src)}R=this.left+(L+0.5)*this.width/this.data.length;return[T,R]};J.prototype.drawXAxisLabel=function(M,O,N){var L;return L=this.raphael.text(M,O,N).attr("font-size",this.options.gridTextSize).attr("font-family",this.options.gridTextFamily).attr("font-weight",this.options.gridTextWeight).attr("fill",this.options.gridTextColor)};J.prototype.drawBar=function(P,L,Q,T,R,O,M){var N,S;N=Math.max.apply(Math,M);if(N===0||N>T){S=this.raphael.rect(P,L,Q,T)}else{S=this.raphael.path(this.roundedRect(P,L,Q,T,M))}return S.attr("fill",R).attr("fill-opacity",O).attr("stroke","none")};J.prototype.roundedRect=function(N,O,P,L,M){if(M==null){M=[0,0,0,0]}return["M",N,M[0]+O,"Q",N,O,N+M[0],O,"L",N+P-M[1],O,"Q",N+P,O,N+P,O+M[1],"L",N+P,O+L-M[2],"Q",N+P,O+L,N+P-M[2],O+L,"L",N+M[3],O+L,"Q",N,O+L,N,O+L-M[3],"Z"]};return J})(H.Grid);H.Donut=(function(J){B(K,J);K.prototype.defaults={colors:["#0B62A4","#3980B5","#679DC6","#95BBD7","#B0CCE1","#095791","#095085","#083E67","#052C48","#042135"],backgroundColor:"#FFFFFF",labelColor:"#000000",formatter:H.commas,resize:false};function K(M){this.resizeHandler=A(this.resizeHandler,this);this.select=A(this.select,this);this.click=A(this.click,this);var L=this;if(!(this instanceof H.Donut)){return new H.Donut(M)}this.options=E.extend({},this.defaults,M);if(typeof M.element==="string"){this.el=E(document.getElementById(M.element))}else{this.el=E(M.element)}if(this.el===null||this.el.length===0){throw new Error("Graph placeholder not found.")}if(M.data===void 0||M.data.length===0){return}this.raphael=new Raphael(this.el[0]);if(this.options.resize){E(window).bind("resize",function(N){if(L.timeoutId!=null){window.clearTimeout(L.timeoutId)}return L.timeoutId=window.setTimeout(L.resizeHandler,100)})}this.setData(M.data)}K.prototype.redraw=function(){var a,P,Z,R,Q,b,e,W,Y,f,c,d,N,S,U,h,V,O,M,X,g,L,T;this.raphael.clear();P=this.el.width()/2;Z=this.el.height()/2;N=(Math.min(P,Z)-10)/3;c=0;X=this.values;for(S=0,V=X.length;S<V;S++){d=X[S];c+=d}W=5/(2*N);a=1.9999*Math.PI-W*this.data.length;b=0;Q=0;this.segments=[];g=this.values;for(R=U=0,O=g.length;U<O;R=++U){d=g[R];Y=b+W+a*(d/c);f=new H.DonutSegment(P,Z,N*2,N,b,Y,this.data[R].color||this.options.colors[Q%this.options.colors.length],this.options.backgroundColor,Q,this.raphael);f.render();this.segments.push(f);f.on("hover",this.select);f.on("click",this.click);b=Y;Q+=1}this.text1=this.drawEmptyDonutLabel(P,Z-10,this.options.labelColor,15,800);this.text2=this.drawEmptyDonutLabel(P,Z+10,this.options.labelColor,14);e=Math.max.apply(Math,this.values);Q=0;L=this.values;T=[];for(h=0,M=L.length;h<M;h++){d=L[h];if(d===e){this.select(Q);break}T.push(Q+=1)}return T};K.prototype.setData=function(M){var L;this.data=M;this.values=(function(){var O,Q,P,N;P=this.data;N=[];for(O=0,Q=P.length;O<Q;O++){L=P[O];N.push(parseFloat(L.value))}return N}).call(this);return this.redraw()};K.prototype.click=function(L){return this.fire("click",L,this.data[L])};K.prototype.select=function(Q){var O,M,P,R,L,N;N=this.segments;for(R=0,L=N.length;R<L;R++){M=N[R];M.deselect()}P=this.segments[Q];P.select();O=this.data[Q];return this.setLabels(O.label,this.options.formatter(O.value,O))};K.prototype.setLabels=function(U,L){var M,N,T,Q,P,S,R,O;M=(Math.min(this.el.width()/2,this.el.height()/2)-10)*2/3;Q=1.8*M;T=M/2;N=M/3;this.text1.attr({text:U,transform:""});P=this.text1.getBBox();S=Math.min(Q/P.width,T/P.height);this.text1.attr({transform:"S"+S+","+S+","+(P.x+P.width/2)+","+(P.y+P.height)});this.text2.attr({text:L,transform:""});R=this.text2.getBBox();O=Math.min(Q/R.width,N/R.height);return this.text2.attr({transform:"S"+O+","+O+","+(R.x+R.width/2)+","+R.y})};K.prototype.drawEmptyDonutLabel=function(L,P,M,N,Q){var O;O=this.raphael.text(L,P,"").attr("font-size",N).attr("fill",M);if(Q!=null){O.attr("font-weight",Q)}return O};K.prototype.resizeHandler=function(){this.timeoutId=null;this.raphael.setSize(this.el.width(),this.el.height());return this.redraw()};return K})(H.EventEmitter);H.DonutSegment=(function(J){B(K,J);function K(R,P,M,L,T,S,O,U,N,Q){this.cx=R;this.cy=P;this.inner=M;this.outer=L;this.color=O;this.backgroundColor=U;this.index=N;this.raphael=Q;this.deselect=A(this.deselect,this);this.select=A(this.select,this);this.sin_p0=Math.sin(T);this.cos_p0=Math.cos(T);this.sin_p1=Math.sin(S);this.cos_p1=Math.cos(S);this.is_long=(S-T)>Math.PI?1:0;this.path=this.calcSegment(this.inner+3,this.inner+this.outer-5);this.selectedPath=this.calcSegment(this.inner+3,this.inner+this.outer);this.hilight=this.calcArc(this.inner)}K.prototype.calcArcPoints=function(L){return[this.cx+L*this.sin_p0,this.cy+L*this.cos_p0,this.cx+L*this.sin_p1,this.cy+L*this.cos_p1]};K.prototype.calcSegment=function(O,R){var V,U,T,S,M,L,Q,P,W,N;W=this.calcArcPoints(O),V=W[0],T=W[1],U=W[2],S=W[3];N=this.calcArcPoints(R),M=N[0],Q=N[1],L=N[2],P=N[3];return("M"+V+","+T)+("A"+O+","+O+",0,"+this.is_long+",0,"+U+","+S)+("L"+L+","+P)+("A"+R+","+R+",0,"+this.is_long+",1,"+M+","+Q)+"Z"};K.prototype.calcArc=function(L){var N,M,P,O,Q;Q=this.calcArcPoints(L),N=Q[0],P=Q[1],M=Q[2],O=Q[3];return("M"+N+","+P)+("A"+L+","+L+",0,"+this.is_long+",0,"+M+","+O)};K.prototype.render=function(){var L=this;this.arc=this.drawDonutArc(this.hilight,this.color);return this.seg=this.drawDonutSegment(this.path,this.color,this.backgroundColor,function(){return L.fire("hover",L.index)},function(){return L.fire("click",L.index)})};K.prototype.drawDonutArc=function(M,L){return this.raphael.path(M).attr({stroke:L,"stroke-width":2,opacity:0})};K.prototype.drawDonutSegment=function(P,N,O,L,M){return this.raphael.path(P).attr({fill:N,stroke:O,"stroke-width":3}).hover(L).click(M)};K.prototype.select=function(){if(!this.selected){this.seg.animate({path:this.selectedPath},150,"<>");this.arc.animate({opacity:1},150,"<>");return this.selected=true}};K.prototype.deselect=function(){if(this.selected){this.seg.animate({path:this.path},150,"<>");this.arc.animate({opacity:0},150,"<>");return this.selected=false}};return K})(H.EventEmitter)}).call(this);