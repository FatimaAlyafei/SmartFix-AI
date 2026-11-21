const $=id=>document.getElementById(id);
const technicianPage=!!$('technicianId');
function node(tag,text){const e=document.createElement(tag);e.textContent=text;return e;}
function message(text,error=false){$('message').textContent=text;$('message').className=error?'error':'success';}
async function get(url){const r=await fetch(url);if(!r.ok)throw new Error(await errorText(r));return r.json();}
async function errorText(r){try{const x=await r.json();return x.detail||x.message||('HTTP '+r.status);}catch{return 'HTTP '+r.status;}}
function card(parent,title,lines){const d=node('article','');d.className='item';d.append(node('h3',title));for(const line of lines)d.append(node('p',line));parent.append(d);return d;}
async function load(){try{
 message('Loading...');$('requests').replaceChildren();
 if(technicianPage){const id=$('technicianId').value;if(!id)throw Error('Enter technician ID');const data=await get('/api/requests?technicianId='+encodeURIComponent(id));for(const r of data){const d=card($('requests'),'Request #'+r.requestId,[r.description,'Location: '+r.location,'Priority: '+(r.priorityLevel||'Not assessed'),'Status: '+r.status]);
 if(r.status==='ASSIGNED'||r.status==='IN_PROGRESS'){
 const next=r.status==='ASSIGNED'?'IN_PROGRESS':'COMPLETED';const b=node('button','Set '+next.replace('_',' '));d.append(b);
 let resolution,cost;if(next==='COMPLETED'){resolution=node('textarea','');resolution.placeholder='Resolution (required)';cost=node('input','');cost.type='number';cost.min='0';cost.step='0.01';cost.placeholder='Total cost (optional)';d.append(resolution,cost);}
 b.onclick=async()=>{try{const payload={status:next};if(next==='COMPLETED'){payload.resolution=resolution.value;payload.totalCost=cost.value===''?null:Number(cost.value);}const resp=await fetch('/api/requests/'+r.requestId+'/status?technicianId='+encodeURIComponent(id),{method:'PATCH',headers:{'Content-Type':'application/json'},body:JSON.stringify(payload)});if(!resp.ok)throw Error(await errorText(resp));message('Request #'+r.requestId+' updated');await load();}catch(e){message(e.message,true);}};
 }}message(data.length?data.length+' request(s) found':'No assigned requests');
 }else{const id=$('clientId').value;if(!id)throw Error('Enter client ID');const [requests,notes,history]=await Promise.all([get('/api/requests?clientId='+encodeURIComponent(id)),get('/api/notifications?recipientId='+encodeURIComponent(id)),get('/api/history?clientId='+encodeURIComponent(id))]);for(const r of requests)card($('requests'),'Request #'+r.requestId,[r.description,'Status: '+r.status.replace('_',' ')]);$('notifications').replaceChildren();for(const n of notes)card($('notifications'),'Notification #'+n.notificationId,[n.message]);$('history').replaceChildren();for(const h of history)card($('history'),'Completed request #'+h.requestId,['Resolution: '+(h.resolution||'Not provided'),'Total cost: '+(h.totalCost??'Not provided')]);message('Tracking information loaded');}
 }catch(e){message(e.message,true);}}
$('load').addEventListener('click',load);
