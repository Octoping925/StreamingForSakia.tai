// 서버를 띄우기 위한 기본 세팅 (express)
const express = require('express');
const fs = require('fs');
const mime = require('mime');
const app = express();

app.listen(8080, () => {
    console.log("listening on 8080");
});

app.get("/", (request, response)=> {
    // response.sendFile(__dirname + "/index.html");
    response.sendFile(__dirname + "/videotest.html");
});

app.get("/video", (request, response)=> {
    response.send("진태를 위한 스트리밍 서버");
});

app.get("/heat", (request, response) => {
    const path = "C:/Users/myc13/Documents/Project/StreamingForSakiatai";
    // const filePath = path + "/Heat1995.mp4";
    // const filePath = FILEPATH + "/ap.mp4";
    const filePath = "F:/as.mp4";
    const mimetype = mime.getType(filePath);

    response.setHeader("Content-disposition", "attachment; filename=" + filePath);
    response.setHeader("Content-type", mime.getType(filePath));

    const filestream = fs.createReadStream(filePath);
    filestream.pipe(response);
});

app.get("/vengeance", (request, response) => {
    const path = "C:/Users/myc13/Documents/Project/StreamingForSakiatai";
    const filePath = "F:/Vengeance.Is.Mine.1979.mp4";
    const mimetype = mime.getType(filePath);

    response.setHeader("Content-disposition", "attachment; filename=" + filePath);
    response.setHeader("Content-type", mime.getType(filePath));

    const filestream = fs.createReadStream(filePath);
    filestream.pipe(response);
});

// 175.119.119.88:8080

// plyr.io