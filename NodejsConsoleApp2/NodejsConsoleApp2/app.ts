
//*****Важные переменные*******//

// конфиг для подключения к БД
const config = {
    port: 1433,
    server: 'LAPTOP-CH2DPNJ6',
    user: 'test',
    password: 'test',
    database: 'DiplomDatabase',
    stream: false,
    options: {
        trustedConnection: true,
        encrypt: true,
        enableArithAbort: true,
        trustServerCertificate: true,

    },
}

var sql = require('mssql');
const http = require('http');
const url = require('url');
var value;

//*****Важные переменные*******//


//Функция вариации sql запросов
function zapros_(key, id_smb, callback) {
    var zapros;
    switch (key) {
        case 1:
           //На достопримечательности
            zapros = `select sight.id_sight as Id_sight,sight.id_region as id_region,name_sight,description,Name_region,face_photo,name_type
            from sight left join region  on sight.Id_region = region.Id_region  FULL OUTER JOIN type_sight ON sight.Id_sight = type_sight.id_sight
            FULL OUTER JOIN type ON type_sight.id_type = type.id_type where sight.Id_region = ${id_smb};`
            break
        case 2:
            // запрос выборку из галерея
            zapros = ` select * from picture where id_sight = ${id_smb}`;
            break
        default:
            zapros = `select * from region`;
    }

    sql.connect(config).then(function () {
        new sql.Request().query(zapros).then(function (recordset) {
            return callback(recordset.recordsets[0]);
        }).catch(function (err) {
            console.dir("error!Sorry(1)");
           console.dir(err);
    	});
    }).catch(function (err) {
        console.dir("error!Sorry(2)");
        console.dir(err);
    });
}
 
 //Создание и прослушивание сервера 
  http.createServer((request, response) => {
      console.log('server work');
     response.writeHead(200, { 'Content-Type': 'application/json' });
      //Позволит реализовывать разные страницы
      var urlRequest = url.parse(request.url, true);
      switch (true) {

          //Раздел список достопримечательностей
          case (request.url.includes("/getRegionSight")):
              //Получение значения из строки запроса
              let id_region = urlRequest.query.test;
              zapros_(1, id_region, function (result) {
                  console.dir(result);
                  value = (JSON.stringify(result));
      
                 response.write(value);
                 response.end();
              
            });
              break
          //Раздел подробней достопримечательностей
          case (request.url.includes("/getSight")):
              //Получение значения из строки запроса
              let id_sigth = urlRequest.query.test;
              zapros_(2, id_sigth, function (result) {
                  value = (JSON.stringify(result));
                  response.write(value);
                  response.end();

              });
              break
          default:
              response.writeHead(404);
              response.end(JSON.stringify({ error: "!Resource not found! :с" }));
      }

  }).listen(3000);//Порт 3000
 


 