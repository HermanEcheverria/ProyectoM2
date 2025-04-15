export async function descargarReporteExcel({ inicio, fin, limite, usuario }) {
  const params = new URLSearchParams({
    inicio,
    fin,
    limite,
    usuario,
  });

  const response = await fetch(`http://localhost:8080/reporte-medicinas/excel?${params.toString()}`);

  if (!response.ok) throw new Error("No se pudo generar el reporte");

  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;
  a.download = "medicinas_reporte.xlsx";
  a.click();
  window.URL.revokeObjectURL(url);
}
