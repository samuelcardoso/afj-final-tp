print("########################### Start ###########################");

db = db.getSiblingDB("productsdb");
db.createUser({
  user: "admin",
  pwd: "pass",
  roles: [{ role: "readWrite", db: "productsdb" }],
});

print("########################### End ###########################");
