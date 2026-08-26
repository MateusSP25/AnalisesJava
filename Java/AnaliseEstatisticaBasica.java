import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AnaliseEstatisticaBasica extends JFrame {

    private final JTextArea inputArea = new JTextArea(
            "12, 15, 18, 20, 22, 22, 24, 25, 28, 30", 5, 50);

    private final JLabel lblN = new JLabel("-");
    private final JLabel lblMedia = new JLabel("-");
    private final JLabel lblMediana = new JLabel("-");
    private final JLabel lblModa = new JLabel("-");
    private final JLabel lblMin = new JLabel("-");
    private final JLabel lblMax = new JLabel("-");
    private final JLabel lblAmplitude = new JLabel("-");
    private final JLabel lblVariancia = new JLabel("-");
    private final JLabel lblDesvio = new JLabel("-");
    private final JLabel lblCV = new JLabel("-");
    private final JLabel lblQ1 = new JLabel("-");
    private final JLabel lblQ3 = new JLabel("-");

    private final JTextArea ordenadosArea = new JTextArea(8, 40);
    private final JTextArea interpretacaoArea = new JTextArea(5, 40);

    private final GraficoFrequencia grafico = new GraficoFrequencia();

    public AnaliseEstatisticaBasica() {
        setTitle("Análise Estatística Básica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 780);
        setLocationRelativeTo(null);

        Color fundo = new Color(15, 23, 42);
        Color painel = new Color(30, 41, 59);
        Color texto = new Color(226, 232, 240);
        Color destaque = new Color(56, 189, 248);

        UIManager.put("Label.foreground", texto);
        UIManager.put("Panel.background", fundo);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(fundo);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("📊 Análise Estatística Básica", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(texto);
        titulo.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(titulo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(fundo);

        // Entrada
        JPanel entrada = criarCard(painel);
        entrada.setLayout(new BorderLayout(10, 10));

        JLabel instrucao = new JLabel(
                "Digite valores separados por vírgula, espaço ou quebra de linha:");
        instrucao.setForeground(texto);
        entrada.add(instrucao, BorderLayout.NORTH);

        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(new Color(15, 23, 42));
        inputArea.setForeground(texto);
        inputArea.setCaretColor(texto);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        entrada.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.setBackground(painel);

        JButton btnAnalisar = new JButton("Analisar");
        JButton btnAleatorios = new JButton("Gerar números aleatórios");

        btnAnalisar.setBackground(destaque);
        btnAleatorios.setBackground(destaque);

        btnAnalisar.addActionListener(e -> analisar());
        btnAleatorios.addActionListener(e -> gerarAleatorios());

        botoes.add(btnAnalisar);
        botoes.add(btnAleatorios);

        entrada.add(botoes, BorderLayout.SOUTH);
        conteudo.add(entrada);
        conteudo.add(Box.createVerticalStrut(12));

        // Estatísticas
        JPanel statsCard = criarCard(painel);
        statsCard.setLayout(new BorderLayout());

        JLabel sub = new JLabel("Estatísticas descritivas");
        sub.setFont(new Font("SansSerif", Font.BOLD, 18));
        sub.setForeground(texto);
        sub.setBorder(new EmptyBorder(0, 0, 10, 0));
        statsCard.add(sub, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(3, 4, 10, 10));
        stats.setBackground(painel);

        adicionarStat(stats, "N", lblN, destaque, texto);
        adicionarStat(stats, "Média", lblMedia, destaque, texto);
        adicionarStat(stats, "Mediana", lblMediana, destaque, texto);
        adicionarStat(stats, "Moda", lblModa, destaque, texto);
        adicionarStat(stats, "Mínimo", lblMin, destaque, texto);
        adicionarStat(stats, "Máximo", lblMax, destaque, texto);
        adicionarStat(stats, "Amplitude", lblAmplitude, destaque, texto);
        adicionarStat(stats, "Variância amostral", lblVariancia, destaque, texto);
        adicionarStat(stats, "Desvio-padrão", lblDesvio, destaque, texto);
        adicionarStat(stats, "Coef. de variação", lblCV, destaque, texto);
        adicionarStat(stats, "Q1", lblQ1, destaque, texto);
        adicionarStat(stats, "Q3", lblQ3, destaque, texto);

        statsCard.add(stats, BorderLayout.CENTER);
        conteudo.add(statsCard);
        conteudo.add(Box.createVerticalStrut(12));

        // Dados ordenados
        JPanel ordenadosCard = criarCard(painel);
        ordenadosCard.setLayout(new BorderLayout());

        JLabel ordTitulo = new JLabel("Dados ordenados");
        ordTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        ordTitulo.setForeground(texto);
        ordenadosCard.add(ordTitulo, BorderLayout.NORTH);

        ordenadosArea.setEditable(false);
        ordenadosArea.setBackground(new Color(15, 23, 42));
        ordenadosArea.setForeground(texto);
        ordenadosArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ordenadosCard.add(new JScrollPane(ordenadosArea), BorderLayout.CENTER);

        conteudo.add(ordenadosCard);
        conteudo.add(Box.createVerticalStrut(12));

        // Gráfico
        JPanel graficoCard = criarCard(painel);
        graficoCard.setLayout(new BorderLayout());

        JLabel grafTitulo = new JLabel("Gráfico de frequências");
        grafTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        grafTitulo.setForeground(texto);
        graficoCard.add(grafTitulo, BorderLayout.NORTH);

        grafico.setPreferredSize(new Dimension(900, 300));
        grafico.setBackground(new Color(15, 23, 42));
        graficoCard.add(grafico, BorderLayout.CENTER);

        conteudo.add(graficoCard);
        conteudo.add(Box.createVerticalStrut(12));

        // Interpretação
        JPanel interpCard = criarCard(painel);
        interpCard.setLayout(new BorderLayout());

        JLabel interpTitulo = new JLabel("Interpretação");
        interpTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        interpTitulo.setForeground(texto);
        interpCard.add(interpTitulo, BorderLayout.NORTH);

        interpretacaoArea.setEditable(false);
        interpretacaoArea.setLineWrap(true);
        interpretacaoArea.setWrapStyleWord(true);
        interpretacaoArea.setBackground(new Color(15, 23, 42));
        interpretacaoArea.setForeground(texto);
        interpretacaoArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        interpCard.add(new JScrollPane(interpretacaoArea), BorderLayout.CENTER);

        conteudo.add(interpCard);

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scroll, BorderLayout.CENTER);
        setContentPane(root);

        analisar();
    }

    private JPanel criarCard(Color painel) {
        JPanel p = new JPanel();
        p.setBackground(painel);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        return p;
    }

    private void adicionarStat(JPanel parent, String nome, JLabel valor,
                               Color destaque, Color texto) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(15, 23, 42));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel(nome);
        titulo.setForeground(destaque);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));

        valor.setForeground(texto);
        valor.setFont(new Font("SansSerif", Font.BOLD, 17));

        p.add(titulo, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);

        parent.add(p);
    }

    private void gerarAleatorios() {
        Random random = new Random();
        int quantidade = 20;
        int min = 1;
        int max = 100;

        List<Integer> numeros = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            numeros.add(random.nextInt(max - min + 1) + min);
        }

        String texto = numeros.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        inputArea.setText(texto);
        analisar();
    }

    private void analisar() {
        try {
            double[] dados = parseDados(inputArea.getText());

            if (dados.length < 2) {
                JOptionPane.showMessageDialog(
                        this,
                        "Informe pelo menos dois valores numéricos.",
                        "Dados insuficientes",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Arrays.sort(dados);

            double media = media(dados);
            double mediana = mediana(dados);
            String moda = moda(dados);
            double minimo = dados[0];
            double maximo = dados[dados.length - 1];
            double amplitude = maximo - minimo;
            double variancia = varianciaAmostral(dados);
            double desvio = Math.sqrt(variancia);
            double cv = media != 0 ? Math.abs(desvio / media) * 100 : Double.NaN;
            double q1 = quartil(dados, 0.25);
            double q3 = quartil(dados, 0.75);

            lblN.setText(String.valueOf(dados.length));
            lblMedia.setText(format(media));
            lblMediana.setText(format(mediana));
            lblModa.setText(moda);
            lblMin.setText(format(minimo));
            lblMax.setText(format(maximo));
            lblAmplitude.setText(format(amplitude));
            lblVariancia.setText(format(variancia));
            lblDesvio.setText(format(desvio));
            lblCV.setText(Double.isFinite(cv) ? format(cv) + "%" : "Indefinido");
            lblQ1.setText(format(q1));
            lblQ3.setText(format(q3));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dados.length; i++) {
                sb.append(String.format("%2d -> %s%n", i + 1, format(dados[i])));
            }
            ordenadosArea.setText(sb.toString());

            String classificacao;
            if (!Double.isFinite(cv)) {
                classificacao = "não pode ser interpretado porque a média é zero";
            } else if (cv < 15) {
                classificacao = "indica baixa dispersão relativa";
            } else if (cv < 30) {
                classificacao = "indica dispersão relativa moderada";
            } else {
                classificacao = "indica alta dispersão relativa";
            }

            interpretacaoArea.setText(
                    "A amostra possui " + dados.length + " observações. "
                            + "A média é " + format(media)
                            + ", a mediana é " + format(mediana)
                            + " e o desvio-padrão amostral é " + format(desvio)
                            + ". O coeficiente de variação é "
                            + (Double.isFinite(cv) ? format(cv) + "%" : "indefinido")
                            + " e " + classificacao + ". "
                            + "Os valores variam de " + format(minimo)
                            + " até " + format(maximo) + "."
            );

            grafico.setDados(dados);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Há valores inválidos. Use apenas números separados por vírgula, espaço ou quebra de linha.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private double[] parseDados(String texto) {
        String[] partes = texto.trim().split("[\\s,;]+");

        List<Double> numeros = new ArrayList<>();

        for (String parte : partes) {
            if (!parte.isBlank()) {
                numeros.add(Double.parseDouble(parte.replace(",", ".")));
            }
        }

        return numeros.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double media(double[] v) {
        return Arrays.stream(v).average().orElse(Double.NaN);
    }

    private double mediana(double[] v) {
        int n = v.length;
        if (n % 2 == 1) {
            return v[n / 2];
        }
        return (v[n / 2 - 1] + v[n / 2]) / 2.0;
    }

    private double varianciaAmostral(double[] v) {
        double m = media(v);
        double soma = 0.0;

        for (double x : v) {
            soma += Math.pow(x - m, 2);
        }

        return soma / (v.length - 1);
    }

    private double quartil(double[] v, double p) {
        double pos = (v.length - 1) * p;
        int base = (int) Math.floor(pos);
        double resto = pos - base;

        if (base + 1 < v.length) {
            return v[base] + resto * (v[base + 1] - v[base]);
        }

        return v[base];
    }

    private String moda(double[] v) {
        Map<Double, Integer> freq = new TreeMap<>();

        for (double x : v) {
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }

        int maior = freq.values().stream().max(Integer::compareTo).orElse(1);

        if (maior == 1) {
            return "Amodal";
        }

        return freq.entrySet().stream()
                .filter(e -> e.getValue() == maior)
                .map(e -> format(e.getKey()))
                .collect(Collectors.joining(", "));
    }

    private String format(double x) {
        return String.format(Locale.US, "%.2f", x);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AnaliseEstatisticaBasica app = new AnaliseEstatisticaBasica();
            app.setVisible(true);
        });
    }

    // --------------------------------------------------------
    // Painel gráfico simples de frequências
    // --------------------------------------------------------
    private static class GraficoFrequencia extends JPanel {

        private double[] dados = new double[0];

        public void setDados(double[] dados) {
            this.dados = Arrays.copyOf(dados, dados.length);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (dados.length == 0) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();

            try {
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Map<Double, Integer> freq = new TreeMap<>();
                for (double x : dados) {
                    freq.put(x, freq.getOrDefault(x, 0) + 1);
                }

                int largura = getWidth();
                int altura = getHeight();

                int margem = 45;
                int maxFreq = Collections.max(freq.values());
                int n = freq.size();

                double espaco = (largura - 2.0 * margem) / n;
                double barra = espaco * 0.65;

                g2.setColor(new Color(148, 163, 184));
                g2.drawLine(
                        margem,
                        altura - margem,
                        largura - margem,
                        altura - margem
                );

                int i = 0;

                for (Map.Entry<Double, Integer> e : freq.entrySet()) {

                    double prop = e.getValue() / (double) maxFreq;
                    int h = (int) ((altura - 100) * prop);

                    int x = (int) (
                            margem + i * espaco + (espaco - barra) / 2
                    );

                    int y = altura - margem - h;

                    g2.setColor(new Color(56, 189, 248));
                    g2.fillRect(
                            x,
                            y,
                            Math.max(1, (int) barra),
                            h
                    );

                    g2.setColor(new Color(226, 232, 240));

                    String valor = String.format(
                            Locale.US,
                            "%.0f",
                            e.getKey()
                    );

                    g2.drawString(
                            valor,
                            x,
                            altura - margem + 18
                    );

                    g2.drawString(
                            String.valueOf(e.getValue()),
                            x,
                            Math.max(15, y - 6)
                    );

                    i++;
                }

            } finally {
                g2.dispose();
            }
        }
    }
}
