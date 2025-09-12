import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import SignUpForm from '@/components/SignUpForm.vue'

describe('SignUpForm.vue', () => {
  it('renderiza los campos requeridos y el botón', () => {
    const wrapper = mount(SignUpForm)

    // Inputs presentes y con "required"
    expect(wrapper.get('input[placeholder="Nombre"]').attributes('required')).toBeDefined()
    expect(wrapper.get('input[placeholder="Correo"]').attributes('required')).toBeDefined()
    expect(wrapper.get('input[placeholder="Contraseña"]').attributes('required')).toBeDefined()

    // Botón presente
    expect(wrapper.find('button[type="submit"]').exists()).toBe(true)

    // Mensaje aún no se muestra (v-if)
    expect(wrapper.find('p').exists()).toBe(false)
  })

  it('al enviar el formulario muestra el mensaje de registro', async () => {
    const wrapper = mount(SignUpForm)

    await wrapper.get('input[placeholder="Nombre"]').setValue('Andy')
    await wrapper.get('input[placeholder="Correo"]').setValue('andy@example.com')
    await wrapper.get('input[placeholder="Contraseña"]').setValue('Secreta123')

    await wrapper.get('form').trigger('submit.prevent')

    const msg = wrapper.get('p')
    expect(msg.text()).toBe('Registro enviado (Falta conectar con backend)')
  })
})
