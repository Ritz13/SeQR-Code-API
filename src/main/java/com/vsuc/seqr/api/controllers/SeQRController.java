package com.vsuc.seqr.api.controllers;

import com.vsuc.seqr.core.SeQREncoder;
import com.vsuc.seqr.core.SeQRRunner;
import com.vsuc.seqr.utils.Result;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Controller
public class SeQRController {

    @GetMapping(path="/encode")
    public @ResponseBody
    ResponseEntity<byte[]> encode(@RequestParam(value = "text", defaultValue = "RandomTextToCreate") String text) throws Exception {
        BufferedImage image = new SeQREncoder().SeQREncode(new String[]{text});

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos );
        byte [] data = bos.toByteArray();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(data);
    }

    @PostMapping(path="/decode", consumes={"multipart/form-data"})
    public @ResponseBody
    ResponseEntity<String> decode(@RequestPart("image") MultipartFile image) throws Exception {

        byte[] byteArr = image.getBytes();
        BufferedImage bImage = createImageFromBytes(byteArr);

        String result =  new SeQRRunner().SeQRDecode(new String[]{""}, bImage);

        return ResponseEntity.ok().body(result);
    }

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
