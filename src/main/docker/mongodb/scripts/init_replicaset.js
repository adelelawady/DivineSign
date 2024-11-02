const status = rs.status();
if (status.errmsg === 'no replset config has been received') {
  rs.initiate();
}
for (let i = 1; i <= param; i++) {
  if (i !== 1) rs.add(`${folder}_divinesign-mongodb-node_${i}:27018`);
}
const cfg = rs.conf();
cfg.members[0].host = `${folder}_divinesign-mongodb-node_1:27018`;
rs.reconfig(cfg);
