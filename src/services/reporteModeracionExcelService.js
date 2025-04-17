export async function descargarReporteModeracionExcel({ fechaInicio, fechaFin, limite }) {
    const params = new URLSearchParams({
      fechaInicio,
      fechaFin,
      limite,
    });

    const response = await fetch(`http://localhost:8080/api/reporte-moderacion/usuarios/excel?${params.toString()}`);

    if (!response.ok) throw new Error("No se pudo generar el reporte");

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = "reporte_usuarios_moderacion.xlsx";
    a.click();
    window.URL.revokeObjectURL(url);
  }
