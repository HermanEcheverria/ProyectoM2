import { createRouter, createWebHistory } from "vue-router";
import type { RouteLocationNormalized, NavigationGuardNext } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AboutView from "../views/AboutView.vue";
import SignUp from "../views/SignUp.vue";
import Login from "../views/Login.vue";
import FaqView from "../views/FaqView.vue";
import ContactView from "../views/ContactView.vue";
import AdminPortal from "../views/AdminPortal.vue";
import AdminFaqView from "../views/admin/AdminFaqView.vue";
import Patient from "../../src/components/Patient.vue";
import AdminUsuarios from "../components/AdminUsuarios.vue";
import MedicalAppointments from "../views/doctores/MedicalAppointments.vue";
import MedicalPrescription from "../views/doctores/MedicalPrescription.vue";
import MedicalSchedule from "../views/doctores/MedicalAppointments.vue";
import MedicalServices from "../views/MedicalServices.vue";
import HistoriaView from "@/views/HistoriaView.vue";
import AdminHistoriaView from "@/views/admin/AdminHistoriaView.vue";
import FichaTecnicaView from "@/views/FichaTecnicaView.vue";
import PacienteAdmin from "../views/PacienteAdmin.vue";
import EmpleadosAdmin from "../views/EmpleadosAdmin.vue";
import AdminServiciosView from "@/views/admin/AdminServiciosView.vue";
import DoctorAdmin from "../views/DoctorAdmin.vue";
import MyAccountAdmin from "../views/MyAccountAdmin.vue";
import MyAccountDoctor from "../views/myAccountDoctor.vue";
import MyAccountEmpleado from "../views/MyAccountEmpleado.vue";
import MyAccountPaciente from "../views/MyAccountPaciente.vue";
import RecetaView from "../views/RecetaView.vue";
import MyAccountUsuarioInter from "@/views/MyAccountUsuarioInter.vue";
import UsuarioInterAdmin from "@/views/UsuarioInterAdmin.vue";
import AdminFichasTecnicas from "@/views/admin/AdminFichasTecnicas.vue";



// Función para verificar si el usuario está autenticado
const requireAuth = (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const userId = localStorage.getItem("userId");

  if (userId) {
    next();
  } else {
    next("/login"); // Redirige al login si no está autenticado
  }
};

// Función para validar el acceso según el rol
const requireRole = (requiredRole: number) => (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const userRole = Number(localStorage.getItem("userRole"));
  if (userRole === requiredRole) {
    next();
  } else {
    next("/"); // Redirige al Home si no tiene acceso
  }
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    //  Rutas generales
    { path: "/", name: "home", component: HomeView },
    { path: "/about", name: "about", component: AboutView },
    { path: "/signup", name: "signup", component: SignUp },
    { path: "/login", name: "login", component: Login },
    { path: "/faq", name: "faq", component: FaqView },
    { path: "/contact", name: "contact", component: ContactView },
    { path: "/servicios-medicos", name: "servicios-medicos", component: MedicalServices },
    { path: "/recetas-pacientes", name: "recetas-pacientes", component: Patient },
    { path: "/historia", name: "historia", component: HistoriaView },

    //  Rutas protegidas por autenticación
    { path: "/admin/crear-ficha-tecnica", name: "ficha-técnica", component: FichaTecnicaView, beforeEnter: requireAuth },
    { path: "/admin-portal", name: "admin-portal", component: AdminPortal, beforeEnter: requireRole(1) },
    { path: "/admin/faq", name: "admin-faq", component: AdminFaqView, beforeEnter: requireRole(1) },
    { path: "/admin-usuarios", name: "admin-usuarios", component: AdminUsuarios, beforeEnter: requireRole(1) },
    { path: "/admin/doctores-recetas", name: "doctores-recetas", component: MedicalPrescription, beforeEnter: requireRole(1) },
    { path: "/admin/doctores-citas", name: "doctores-citas", component: MedicalAppointments, beforeEnter: requireRole(1) },
    { path: "/admin/doctores-agenda", name: "doctores-agenda", component: MedicalSchedule, beforeEnter: requireRole(1) },
    { path: "/admin/historia", name: "admin-historia", component: AdminHistoriaView, beforeEnter: requireRole(1) },
    { path: "/admin/pacientes", name: "admin-pacientes", component: PacienteAdmin, beforeEnter: requireRole(1) },
    { path: "/admin/empleados", name: "admin-empleados", component: EmpleadosAdmin, beforeEnter: requireRole(1) },
    { path: "/admin/servicios", component: AdminServiciosView, beforeEnter: requireRole(1) },
    { path: "/admin/doctor", name: "admin-doctor", component: DoctorAdmin, beforeEnter: requireRole(1) },
    { path: "/admin/usuario-interconexion", name: "admin-usuario-interconexion", component: UsuarioInterAdmin, beforeEnter: requireRole(1) },
    { path: "/admin/fichas-tecnicas", name: "admin-fichas-tecnicas", component: AdminFichasTecnicas, beforeEnter: requireRole(1) },

    //  Rutas protegidas de "My Account" según el rol
    { path: "/my-account-admin", name: "my-account-admin", component: MyAccountAdmin, beforeEnter: requireRole(1) },
    { path: "/my-account-doctor", name: "my-account-doctor", component: MyAccountDoctor, beforeEnter: requireRole(2) },
    { path: "/my-account-empleado", name: "my-account-empleado", component: MyAccountEmpleado, beforeEnter: requireRole(3) },
    { path: "/my-account-paciente", name: "my-account-paciente", component: MyAccountPaciente, beforeEnter: requireRole(4) },
    { path: "/my-account-interconexion", name: "my-account-interconexion", component: MyAccountUsuarioInter, beforeEnter: requireRole(5) },
    { path: "/recetas", name: "recetas", component: RecetaView, beforeEnter: requireRole(1) },

  ]
});

export default router;


