
public class SoundManager extends ThreadPool {

    private AudioFormat playbackFormat;
    private ThreadLocal localLine;
    private ThreadLocal localBuffer;
    private Object pausedLock;
    private boolean paused;

   
    public SoundManager(AudioFormat playbackFormat) {
        this(playbackFormat,
            getMaxSimultaneousSounds(playbackFormat));
    }

    public SoundManager(AudioFormat playbackFormat,
        int maxSimultaneousSounds)
    {
        super(Math.min(maxSimultaneousSounds,
            getMaxSimultaneousSounds(playbackFormat)));
        this.playbackFormat = playbackFormat;
        localLine = new ThreadLocal();
        localBuffer = new ThreadLocal();
        pausedLock = new Object();
        // powiedomienie w�tk�w w puli
        synchronized (this) {
            notifyAll();
        }
    }

   
    public static int getMaxSimultaneousSounds(
        AudioFormat playbackFormat)
    {
        DataLine.Info lineInfo = new DataLine.Info(
            SourceDataLine.class, playbackFormat);
        Mixer mixer = AudioSystem.getMixer(null);
        return mixer.getMaxLines(lineInfo);
    }


    protected void cleanUp() {
         setPaused(false);

        Mixer mixer = AudioSystem.getMixer(null);
        if (mixer.isOpen()) {
            mixer.close();
        }
    }

    public void close() {
        cleanUp();
        super.close();
    }


    public void join() {
        cleanUp();
        super.join();
    }

    public void setPaused(boolean paused) {
        if (this.paused != paused) {
            synchronized (pausedLock) {
                this.paused = paused;
                if (!paused) {
                    // przywr�cenie d�wi�k�w
                    pausedLock.notifyAll();
                }
            }
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public Sound getSound(String filename) {
        return getSound(getAudioInputStream(filename));
    }

    public Sound getSound(InputStream is) {
        return getSound(getAudioInputStream(is));
    }

   
    public Sound getSound(AudioInputStream audioStream) {
        if (audioStream == null) {
            return null;
        }

        int length = (int)(audioStream.getFrameLength() *
            audioStream.getFormat().getFrameSize());

        byte[] samples = new byte[length];
        DataInputStream is = new DataInputStream(audioStream);
        try {
            is.readFully(samples);
            is.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Sound(samples);
    }

    public AudioInputStream getAudioInputStream(String filename) {
        try {
            return getAudioInputStream(
                new FileInputStream(filename));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public AudioInputStream getAudioInputStream(InputStream is) {

        try {
            if (!is.markSupported()) {
                is = new BufferedInputStream(is);
            }
            
            AudioInputStream source =
                AudioSystem.getAudioInputStream(is);

            
            return AudioSystem.getAudioInputStream(
                playbackFormat, source);
        }
        catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return null;
    }

  
    public InputStream play(Sound sound) {
        return play(sound, null, false);
    }

    public InputStream play(Sound sound, SoundFilter filter,
        boolean loop)
    {
        InputStream is;
        if (sound != null) {
            if (loop) {
                is = new LoopingByteInputStream(
                    sound.getSamples());
            }
            else {
                is = new ByteArrayInputStream(sound.getSamples());
            }

            return play(is, filter);
        }
        return null;
    }

  
    public InputStream play(InputStream is) {
        return play(is, null);
    }

    public InputStream play(InputStream is, SoundFilter filter) {
        if (is != null) {
            if (filter != null) {
                is = new FilteredSoundStream(is, filter);
            }
            runTask(new SoundPlayer(is));
        }
        return is;
    }

    protected void threadStarted() {
        synchronized (this) {
            try {
                wait();
            }
            catch (InterruptedException ex) { }
        }

       int bufferSize = playbackFormat.getFrameSize() *
            Math.round(playbackFormat.getSampleRate() / 10);

       SourceDataLine line;
        DataLine.Info lineInfo = new DataLine.Info(
            SourceDataLine.class, playbackFormat);
        try {
            line = (SourceDataLine)AudioSystem.getLine(lineInfo);
            line.open(playbackFormat, bufferSize);
        }
        catch (LineUnavailableException ex) {
            Thread.currentThread().interrupt();
            return;
        }

        line.start();

        byte[] buffer = new byte[bufferSize];

     
        localLine.set(line);
        localBuffer.set(buffer);
    }

    protected void threadStopped() {
        SourceDataLine line = (SourceDataLine)localLine.get();
        if (line != null) {
            line.drain();
            line.close();
        }
    }
    protected class SoundPlayer implements Runnable {

        private InputStream source;

        public SoundPlayer(InputStream source) {
            this.source = source;
        }

        public void run() {
            
            SourceDataLine line = (SourceDataLine)localLine.get();
            byte[] buffer = (byte[])localBuffer.get();
            if (line == null || buffer == null) {
                
                return;
            }

            try {
                int numBytesRead = 0;
                while (numBytesRead != -1) {
                  
                    synchronized (pausedLock) {
                        if (paused) {
                            try {
                                pausedLock.wait();
                            }
                            catch (InterruptedException ex) {
                                return;
                            }
                        }
                    }
                    
                    numBytesRead =
                        source.read(buffer, 0, buffer.length);
                    if (numBytesRead != -1) {
                        line.write(buffer, 0, numBytesRead);
                    }
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
