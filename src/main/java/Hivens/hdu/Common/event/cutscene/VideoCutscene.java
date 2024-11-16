package Hivens.hdu.Common.event.cutscene;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bytedeco.javacv.Java2DFrameUtils;

public class VideoCutscene {
    private ResourceLocation videoResource;
    private FFmpegFrameGrabber frameGrabber;
    private OpenCVFrameConverter.ToMat converterToMat;
    private DynamicTexture videoTexture;
    private ResourceLocation textureLocation;

    private int videoWidth;
    private int videoHeight;
    private double fps;
    private long lastFrameTime = 0;

    private boolean isPlaying = false;
    private boolean isPaused = false;
    private File tempVideoFile;

    // Новые поля для режимов
    private CutsceneMode playMode = CutsceneMode.SINGLE_PLAY;
    private int repeatCount = 0;
    private int maxRepeats = 3;

    // Конструкторы
    public VideoCutscene(ResourceLocation videoResource) {
        this.videoResource = videoResource;
        this.playMode = CutsceneMode.SINGLE_PLAY;
    }

    public VideoCutscene(ResourceLocation videoResource, CutsceneMode mode) {
        this.videoResource = videoResource;
        this.playMode = mode;
    }

    public VideoCutscene(ResourceLocation videoResource, int maxRepeats) {
        this.videoResource = videoResource;
        this.playMode = CutsceneMode.REPEAT_LIMITED;
        this.maxRepeats = maxRepeats;
    }

    public void initVideo() {
        try {
            prepareVideoFile();

            frameGrabber = new FFmpegFrameGrabber(tempVideoFile);
            frameGrabber.start();

            videoWidth = frameGrabber.getImageWidth();
            videoHeight = frameGrabber.getImageHeight();
            fps = frameGrabber.getFrameRate();

            converterToMat = new OpenCVFrameConverter.ToMat();

            // Создаем пустой NativeImage нужного размера
            NativeImage nativeImage = new NativeImage(
                    NativeImage.Format.RGBA,
                    videoWidth,
                    videoHeight,
                    false
            );

            videoTexture = new DynamicTexture(nativeImage);
            textureLocation = Minecraft.getInstance().getTextureManager().register("cutscene_video", videoTexture);

            MinecraftForge.EVENT_BUS.register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareVideoFile() {
        try {
            InputStream inputStream = Minecraft.getInstance()
                    .getResourceManager()
                    .getResource(videoResource)
                    .get()
                    .open();

            tempVideoFile = File.createTempFile("cutscene_", ".mp4");
            tempVideoFile.deleteOnExit();

            Files.copy(inputStream, tempVideoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processFrame(BufferedImage bufferedImage) {
        try {
            // Преобразование BufferedImage в NativeImage
            NativeImage nativeImage = convertBufferedImageToNativeImage(bufferedImage);

            // Обновляем существующую текстуру
            videoTexture.setPixels(nativeImage);
            videoTexture.upload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private NativeImage convertBufferedImageToNativeImage(BufferedImage bufferedImage) {
        try {
            // Создаем ByteArrayOutputStream для записи изображения
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);

            // Преобразуем в InputStream
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            // Создаем NativeImage
            return NativeImage.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        if (!isPlaying || isPaused) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime < (1000 / fps)) return;

        try {
            Frame frame = frameGrabber.grab();
            if (frame == null) {
                end();
                return;
            }

            org.bytedeco.opencv.opencv_core.Mat mat = converterToMat.convert(frame);
            BufferedImage bufferedImage = matToBufferedImage(mat);

            // Обновление текстуры с использованием NativeImage
            processFrame(bufferedImage);

            // Рендеринг текстуры
            renderVideoTexture(event.getGuiGraphics());

            lastFrameTime = currentTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage matToBufferedImage(org.bytedeco.opencv.opencv_core.Mat mat) {
        if (mat == null || mat.empty()) {
            return null;
        }

        try {
            // Используем JavaCV для конвертации в BufferedImage
            return Java2DFrameUtils.toBufferedImage(mat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void renderVideoTexture(GuiGraphics graphics) {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int x = (screenWidth - videoWidth) / 2;
        int y = (screenHeight - videoHeight) / 2;

        graphics.blit(textureLocation, x, y, 0, 0, videoWidth, videoHeight, videoWidth, videoHeight);
    }

    public void start() {
        initVideo();
        isPlaying = true;
        isPaused = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void end() {
        switch (playMode) {
            case SINGLE_PLAY:
                stopAndCleanup();
                break;

            case LOOP:
                try {
                    frameGrabber.setFrameNumber(0);
                    isPlaying = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    stopAndCleanup();
                }
                break;

            case REPEAT_LIMITED:
                repeatCount++;
                if (repeatCount >= maxRepeats) {
                    stopAndCleanup();
                } else {
                    try {
                        frameGrabber.setFrameNumber(0);
                        isPlaying = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        stopAndCleanup();
                    }
                }
                break;
        }
    }

    private void stopAndCleanup() {
        isPlaying = false;
        try {
            frameGrabber.stop();
            frameGrabber.release();

            Minecraft.getInstance().getTextureManager().release(textureLocation);

            MinecraftForge.EVENT_BUS.unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Дополнительные геттеры и сеттеры
    public void setPlayMode(CutsceneMode mode) {
        this.playMode = mode;
    }

    public void setMaxRepeats(int maxRepeats) {
        if (playMode == CutsceneMode.REPEAT_LIMITED) {
            this.maxRepeats = maxRepeats;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public CutsceneMode getPlayMode() {
        return playMode;
    }
}