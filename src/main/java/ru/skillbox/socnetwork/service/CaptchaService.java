package ru.skillbox.socnetwork.service;

import cn.apiclub.captcha.Captcha;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.model.rqdto.CaptchaDto;
import ru.skillbox.socnetwork.model.rqdto.RegisterDto;

import java.util.HashMap;

@Service
public class CaptchaService {

    private static final HashMap<Long, CaptchaDto> captchaDtoHashMap = new HashMap<>();

    private void addCaptcha(CaptchaDto captchaDto) {
        captchaDtoHashMap.put(captchaDto.getId(), captchaDto);
    }

    public CaptchaDto returnNewCaptcha() {

        CaptchaDto captchaDto = new CaptchaDto();
        setupCaptcha(captchaDto);
        this.addCaptcha(captchaDto);
        return captchaDto;
    }

    public void removeCaptcha(Long id) {
        captchaDtoHashMap.remove(id);
    }

    public boolean isCorrectCode(RegisterDto registerDto) {
        if (captchaDtoHashMap.isEmpty()) {
            return false;
        }
        CaptchaDto captchaDto = captchaDtoHashMap.get(registerDto.getCodeId());
        captchaDtoHashMap.remove(registerDto.getCodeId());
        if (captchaDtoHashMap.size() > 50) {
            this.clearCaptchaMap();
        }
        if (captchaDto == null) {
            return false;
        }
        return captchaDto.getHidden().equals(registerDto.getCode());
    }

    private void clearCaptchaMap() {
        HashMap<Long, CaptchaDto> map = new HashMap<>();
        captchaDtoHashMap.forEach((k, v) -> {
            if (k > System.currentTimeMillis() + Constants.FIFTY_SECONDS_IN_MILLIS) {
                map.put(k, v);
            }
        });
        captchaDtoHashMap.clear();
        captchaDtoHashMap.putAll(map);
    }

    private void setupCaptcha(CaptchaDto captchaDto) {
        Captcha captcha = CaptchaUtils.createCaptcha(200, 50);
        captchaDto.setHidden(captcha.getAnswer());
        captchaDto.setImage(CaptchaUtils.encodeBase64(captcha));
    }
}
